package com.github.continuedev.continueeclipseextension;

import com.github.continuedev.continueeclipseextension.constants.MessageTypes;
import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.github.continuedev.continueeclipseextension.services.TelemetryService;
import com.github.continuedev.continueeclipseextension.utils.uuid;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.function.Consumer;

public class CoreMessenger {

    private final Project project;
    private final String continueCorePath;
    private final IdeProtocolClient ideProtocolClient;
    private final CoroutineScope coroutineScope;

    private Writer writer;
    private BufferedReader reader;
    private Process process;
    private final Gson gson = new Gson();
    private final Map<String, (Any?) -> Unit> responseListeners = new HashMap<>();
    private final boolean useTcp;

    public CoreMessenger(@NotNull Project project, @NotNull String continueCorePath, @NotNull IdeProtocolClient ideProtocolClient, @NotNull CoroutineScope coroutineScope) {
        this.project = project;
        this.continueCorePath = continueCorePath;
        this.ideProtocolClient = ideProtocolClient;
        this.coroutineScope = coroutineScope;
        
        init();
    }

    private void init() throws IOException {
        if (useTcp) {
            Socket socket = new Socket("127.0.0.1", 3000);
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.writer = writer;
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.reader = reader;

            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        String line = reader.readLine();
                        if (line != null && !line.isEmpty()) {
                            handleMessage(line);
                        } else {
                            Thread.sleep(100);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("TCP Connection Error: Unable to connect to 127.0.0.1:3000");
                    System.err.println("Reason: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                        writer.close();
                    } catch (IOException e) {
                        System.err.println("Error closing streams: " + e);
                    }
                }
            });
            thread.start();
        } else {
            // Set proper permissions
            coroutineScope.launch(Dispatchers.IO) { setPermissions(continueCorePath) };

            // Start the subprocess
            ProcessBuilder processBuilder =
                    new ProcessBuilder(continueCorePath).directory(File(continueCorePath).parentFile);
            process = processBuilder.start();

            OutputStream outputStream = process.getOutputStream();
            InputStream inputStream = process.getInputStream();

            writer = new PrintWriter(outputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            process.onExit().thenRun(() -> {
                exitCallbacks.forEach(callback -> callback.run());
                var err = process.getErrorStream() != null
                        ? process.getErrorStream().bufferedReader().readText()
                                .trim()
                        : "";
                if (err != null) {
                    // There are often "⚡️Done in Xms" messages, and we want everything after the last one
                    int delimiterIndex = err.lastIndexOf("⚡ Done in");
                    if (delimiterIndex != -1) {
                        err = err.substring(delimiterIndex + "⚡ Done in".length());
                    }
                }

                System.err.println("Core process exited with output: " + err);

                // Log the cause of the failure
                TelemetryService telemetryService = project.getService(TelemetryService.class);
                telemetryService.capture("jetbrains_core_exit", Map.of("error" -> err));

                // Clean up all resources
                writer.close();
                reader.close();
                outputStream.close();
                inputStream.close();
                process.destroy();
            });

            coroutineScope.launch(Dispatchers.IO) {
                try {
                    while (true) {
                        String line = reader.readLine();
                        if (line != null && !line.isEmpty()) {
                            handleMessage(line);
                        } else {
                            Thread.sleep(100);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error handling message: " + e);
                } finally {
                    try {
                        reader.close();
                        writer.close();
                    } catch (IOException e) {
                        System.err.println("Error closing streams: " + e);
                    }
                }
            };
        }
    }
    
    //public void write(@NotNull String message) {
    public void write(String message) {
        try {
            writer.write(message + "\r\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error writing to Continue core: " + e);
        }
    }

    //public void request(@NotNull String messageType, @Nullable Object data, @Nullable String messageId, @NotNull Consumer<Object> onResponse) {
    public void request(String messageType, Object data, String messageId, Consumer<Object> onResponse) {
        String id = messageId != null ? messageId : uuid();
        String message =
                gson.toJson(Map.of("messageId", id, "messageType", messageType, "data", data));
        responseListeners.put(id, onResponse);
        write(message);
    }

    //private void handleMessage(@NotNull String json) {
    private void handleMessage(String json) {
        Map<String, Object> responseMap = gson.fromJson(json, Map.class);
        String messageId = (String) responseMap.get("messageId");
        String messageType = (String) responseMap.get("messageType");
        @Nullable Object data = responseMap.get("data");

        // IDE listeners
        if (MessageTypes.ideMessageTypes.contains(messageType)) {
            ideProtocolClient.handleMessage(json, data);
        }

        // Forward to webview
        if (MessageTypes.PASS_THROUGH_TO_WEBVIEW.contains(messageType)) {
            // TODO: Currently we aren't set up to receive a response back from the webview
            // Can circumvent for getDefaultsModelTitle here for now
            if (messageType == "getDefaultModelTitle") {
                ContinueExtensionSettings continueSettingsService = project.getService(ContinueExtensionSettings.class);
                String defaultModelTitle = continueSettingsService.continueState.lastSelectedInlineEditModel;
                message =
                        gson.toJson(Map.of("messageId", messageId, "messageType", messageType, "data", defaultModelTitle));
                write(message);
            }
            ContinuePluginService continuePluginService = project.getService(ContinuePluginService.class);
            continuePluginService.sendToWebview(messageType, responseMap.get("data"), messageType);
        }

        // Responses for messageId
        responseListeners.remove(messageId).accept(data);
    }

    private void setPermissions(@NotNull String destination) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac") || osName.contains("darwin")) {
            ProcessBuilder processBuilder =
                    new ProcessBuilder("xattr", "-dr", "com.apple.quarantine", destination);
            processBuilder.start();
            setFilePermissions(destination, "rwxr-xr-x");
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            setFilePermissions(destination, "rwxr-xr-x");
        }
    }

    private void setFilePermissions(@NotNull String path, @NotNull String posixPermissions) {
        Set<PosixFilePermission> perms = new HashSet<>();
        if (posixPermissions.contains("r")) perms.add(PosixFilePermission.OWNER_READ);
        if (posixPermissions.contains("w")) perms.add(PosixFilePermission.OWNER_WRITE);
        if (posixPermissions.contains("x")) perms.add(PosixFilePermission.OWNER_EXECUTE);
        try {
            Files.setPosixFilePermissions(Paths.get(path), perms);
        } catch (IOException e) {
            System.err.println("Error setting file permissions: " + e);
        }
    }

    private final List<Runnable> exitCallbacks = new ArrayList<>();

    public void onDidExit(@NotNull Runnable callback) {
        exitCallbacks.add(callback);
    }

    public void killSubprocess() throws IOException {
        if (process != null && process.isAlive()) {
            exitCallbacks.clear();
            process.destroy();
        }
    }
}
