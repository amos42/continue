package com.github.continuedev.continueeclipseextension;

import com.github.continuedev.continueeclipseextension.services.TelemetryService;
import com.github.continuedev.continueeclipseextension.utils.getMachineUniqueID;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.components.service;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import java.nio.file.Paths;
import kotlinx.coroutines.*;

class CoreMessengerManager {
    private Project project;
    private IdeProtocolClient ideProtocolClient;
    private CoroutineScope coroutineScope;

    public CoreMessengerManager(Project project, IdeProtocolClient ideProtocolClient, CoroutineScope coroutineScope) {
        this.project = project;
        this.ideProtocolClient = ideProtocolClient;
        this.coroutineScope = coroutineScope;
        
        init();
    }

    private void init() {
        coroutineScope.launch {
            String myPluginId = "com.github.continuedev.continueeclipsejextension";
            PluginDescriptor pluginDescriptor = PluginManager.getPlugin(PluginId.getId(myPluginId));
            if (pluginDescriptor == null) throw new Exception("Plugin not found");

            String pluginPath = pluginDescriptor.pluginPath;
            String osName = System.getProperty("os.name").toLowerCase();
            String os = "";
            switch (osName) {
                case "mac":
                case "darwin":
                    os = "darwin";
                    break;
                case "win":
                    os = "win32";
                    break;
                case "nix":
                case "nux":
                case "aix":
                    os = "linux";
                    break;
                default:
                    os = "linux";
            }

            String osArch = System.getProperty("os.arch").toLowerCase();
            String arch = "";
            switch (osArch) {
                case "aarch64":
                case "arm64":
                    arch = "arm64";
                    break;
                case "amd64":
                case "x86_64":
                    arch = "x64";
                    break;
                default:
                    arch = "x64";
            }

            String target = "$os-$arch";
            System.out.println("Identified OS: $os, Arch: $arch");
            String corePath = Paths.get(pluginPath.toString(), "core").toString();
            String targetPath = Paths.get(corePath, target).toString();
            String continueCorePath = Paths.get(targetPath, "continue-binary" + (os == "win32" ? ".exe" : "")).toString();

            setupCoreMessenger(continueCorePath);
        }
    }

    private void setupCoreMessenger(String continueCorePath) {
        CoreMessenger coreMessenger = CoreMessenger.create(project, continueCorePath, ideProtocolClient, coroutineScope);
        coreMessenger.request("config/getSerializedProfileInfo", null, null) { response ->
            Map<String, Object> responseObject = (Map<String, Object>) response;
            Map<String, Object> responseContent = (Map<String, Object>) responseObject.get("content");
            Map<String, Object> result = (Map<String, Object>) responseContent.get("result");
            Map<String, Object> config = (Map<String, Object>) result.get("config");
            Boolean allowAnonymousTelemetry = (Boolean) config.get("allowAnonymousTelemetry");

            TelemetryService telemetryService = service(TelemetryService.class);
            if (allowAnonymousTelemetry == true || allowAnonymousTelemetry == null) {
                telemetryService.setup(getMachineUniqueID());
            }
        };
    }

    // On exit, use exponential backoff to create another CoreMessenger
    public void onDidExit() {
        lastBackoffInterval *= 2;
        System.out.println("CoreMessenger exited, retrying in " + lastBackoffInterval + " seconds");
        try {
            Thread.sleep((long) (lastBackoffInterval * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setupCoreMessenger(continueCorePath);
    }

    private static CoreMessenger create(Project project, String continueCorePath, IdeProtocolClient ideProtocolClient, CoroutineScope coroutineScope) {
        return new CoreMessenger(project, continueCorePath, ideProtocolClient, coroutineScope);
    }
}