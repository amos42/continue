package com.github.continuedev.continueeclipseextension;

import com.github.continuedev.continueeclipseextension.Types.GetGhTokenArgs;
import com.github.continuedev.continueeclipseextension.Types.IDE;
import com.github.continuedev.continueeclipseextension.Types.IdeCallback;
import com.github.continuedev.continueeclipseextension.Types.IdeInfo;
import com.github.continuedev.continueeclipseextension.Types.IdeSettings;
import com.github.continuedev.continueeclipseextension.Types.IdeType;
import com.github.continuedev.continueeclipseextension.Types.Location;
import com.github.continuedev.continueeclipseextension.Types.Range;
import com.github.continuedev.continueeclipseextension.Types.RangeInFile;
import com.github.continuedev.continueeclipseextension.Types.ToastType;
import com.github.continuedev.continueeclipseextension.constants.GetContinueGlobalPath;
import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.github.continuedev.continueeclipseextension.utils.OS;
import com.github.continuedev.continueeclipseextension.utils.GetMachineUniqueID;
import com.google.gson.Gson;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class EclipseIde implements IDE {
    private final IProject project;
    private final ContinuePluginService continuePluginService;
    private final String ripgrep;

    public EclipseIde(IProject project, ContinuePluginService continuePluginService) throws Exception {
        this.project = project;
        this.continuePluginService = continuePluginService;

        String myPluginId = "com.github.continuedev.continueeclipseextension";
        Bundle bundle = Platform.getBundle(myPluginId);
        if (bundle == null) {
            throw new Exception("Plugin not found");
        }

        URL rgUrl = FileLocator.find(bundle, new Path("ripgrep/bin/rg" + (OS.WINDOWS ? ".exe" : "")), null);
        URI rgUri = FileLocator.resolve(rgUrl).toURI();
        ripgrep = rgUri.getPath();
    }

    @Override
    public IdeInfo getIdeInfo() {
        String ideName = "Eclipse IDE";
        String ideVersion = Platform.getBundle("org.eclipse.platform").getVersion().toString();
        String remoteName = "local"; // Eclipse 자체는 SSH 연결을 직접 처리하지 않으므로 loca로 설정
        String extensionVersion = Platform.getBundle("com.github.continuedev.continueeclipseextension").getVersion().toString();

        return new IdeInfo(IdeType.JETBRAINS, ideName, ideVersion, remoteName, extensionVersion);
    }

    public boolean enableHubContinueDev() {
        return true;
    }

    @Override
    public IdeSettings getIdeSettings() {
        ContinueExtensionSettings settings = new ContinueExtensionSettings();

        return new IdeSettings(
                settings.getContinueState().getRemoteConfigServerUrl(),
                settings.getContinueState().getRemoteConfigSyncPeriod(),
                settings.getContinueState().getUserToken() != null ? settings.getContinueState().getUserToken() : "",
                settings.getContinueState().isEnableContinueTeamsBeta(),
                "production",
                false // TODO: Needs to be implemented
        );
    }

    @Override
    public List<String> getDiff(boolean includeUnstaged) throws IOException {
        List<String> workspaceDirs = workspaceDirectories();
        List<String> diffs = new ArrayList<>();

        for (String workspaceDir : workspaceDirs) {
            List<String> command = includeUnstaged ? Arrays.asList("git", "diff") : Arrays.asList("git", "diff", "--cached");
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(new File(workspaceDir));
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();

            diffs.add(output.toString());
        }

        return diffs;
    }

    @Override
    public Map<String, String> getClipboardContent() throws Exception {
        org.eclipse.swt.dnd.Clipboard clipboard = new org.eclipse.swt.dnd.Clipboard(Display.getCurrent());
        String text = (String) clipboard.getContents(org.eclipse.swt.dnd.TextTransfer.getInstance());
        clipboard.dispose();
        if (text == null) {
            text = "";
        }
        return Collections.singletonMap("text", text);
    }

    @Override
    public boolean isTelemetryEnabled() {
        return true;
    }

    @Override
    public String getUniqueId() {
        return GetMachineUniqueID.execute();
    }

    @Override
    public String getTerminalContents() {
        return "";
    }

    @Override
    public String getDebugLocals(int threadIndex) {
        throw new UnsupportedOperationException("getDebugLocals not implemented yet");
    }

    @Override
    public List<String> getTopLevelCallStackSources(int threadIndex, int stackDepth) {
        throw new UnsupportedOperationException("getTopLevelCallStackSources not implemented");
    }

    @Override
    public List<Thread> getAvailableThreads() {
        throw new UnsupportedOperationException("getAvailableThreads not implemented yet");
    }

    @Override
    public List<String> getWorkspaceDirs() {
        return Arrays.asList(workspaceDirectories());
    }

    @Override
    public List<ContinueRcJson> getWorkspaceConfigs() throws IOException {
        List<String> workspaceDirs = workspaceDirectories();
        List<String> configs = new ArrayList<>();

        for (String workspaceDir : workspaceDirs) {
            File dir = new File(workspaceDir);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().endsWith(".continuerc.json")) {
                            String fileContent = new String(Files.readAllBytes(file.toPath()), Charset.forName("UTF-8"));
                            configs.add(fileContent);
                        }
                    }
                }
            }
        }

        return configs.stream().map(json -> new Gson().fromJson(json, ContinueRcJson.class)).collect(Collectors.toList());
    }

    @Override
    public boolean fileExists(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    @Override
    public void writeFile(String path, String contents) throws IOException {
        Files.write(Paths.get(path), contents.getBytes(Charset.forName("UTF-8")));
    }

    @Override
    public void showVirtualFile(String title, String contents) throws PartInitException {
        Display.getDefault().asyncExec(() -> {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            try {
                IDE.openEditor(page, new LightVirtualFile(title, contents));
            } catch (PartInitException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String getContinueDir() {
        return GetContinueGlobalPath.execute();
    }

    @Override
    public void openFile(String path) throws PartInitException {
        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
        Display.getDefault().asyncExec(() -> {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            try {
                IDE.openEditor(page, file, true);
            } catch (PartInitException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void openUrl(String url) {
        Display.getDefault().asyncExec(() -> PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url));
    }

    @Override
    public void runCommand(String command) {
        throw new UnsupportedOperationException("runCommand not implemented in Eclipse");
    }

    @Override
    public void saveFile(String filepath) {
        Display.getDefault().asyncExec(() -> {
            IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filepath));
            if (file != null && file.isDirty()) {
                try {
                    // file.save(null, true);
                    file.refreshLocal(IResource.DEPTH_ONE, null);
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public String readFile(String filepath) {
        try {
            File file = new File(filepath);
            if (!file.exists() || file.isDirectory()) {
                return "";
            }
            return new String(Files.readAllBytes(file.toPath()), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String readRangeInFile(String filepath, Range range) throws Exception {    
        String fullContents = readFile(filepath);
        List<String> lines = Arrays.asList(fullContents.split("\n"));
        int startLine = range.getStart().getLine();
        int startCharacter = range.getStart().getCharacter();
        int endLine = range.getEnd().getLine();
        int endCharacter = range.getEnd().getCharacter();

        String firstLine = lines.getOrDefault(startLine, "").substring(startCharacter);
        String lastLine = lines.getOrDefault(endLine, "").substring(0, endCharacter);
        String betweenLines = (endLine - startLine > 1) ? lines.subList(startLine + 1, endLine).stream().collect(Collectors.joining("\n")) : "";

        return Stream.of(firstLine, betweenLines, lastLine)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void showLines(String filepath, int startLine, int endLine) {
        setFileOpen(filepath, true);
    }

    @Override
    public void showDiff(String filepath, String newContents, int stepIndex) throws Exception {
        continuePluginService.getDiffManager().showDiff(filepath, newContents, stepIndex);
    }

    @Override
    public List<String> getOpenFiles() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart[] editors = page.getEditors();
        List<String> result = new ArrayList<>();
        for (IEditorPart editor : editors) {
            if (editor.getEditorInput() instanceof FileEditorInput) {
                FileEditorInput input = (FileEditorInput) editor.getEditorInput();
                result.add(input.getFile().getFullPath().toString());
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> getCurrentFile() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        ITextEditor editor = (ITextEditor) page.getActiveEditor();
        if (editor == null || editor.getEditorInput() == null) {
            return null;
        }

        IFile file = ((FileEditorInput) editor.getEditorInput()).getFile();
        String path = file.getFullPath().toString();
        String contents = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();

        return Map.of(
                "path", path,
                "contents", contents,
                "isUntitled", false
        );
    }

    @Override
    public List<String> getPinnedFiles() {
        // Eclipse에서는 pinned files가 불리고 있지 않으므로, 열린 파일을 반환
        return getOpenFiles();
    }

    @Override
    public String getSearchResults(String query) throws IOException {
        List<String> command = Arrays.asList(ripgrep, "-i", "-C", "2", "--", query, ".");
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(project.getLocation().toFile());
        Process process = builder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        process.waitFor();

        return output.toString();
    }

    @Override
    public List<String> subprocess(String command, String cwd) throws IOException {
        List<String> commandList = Arrays.asList(command.split(" "));
        ProcessBuilder builder = new ProcessBuilder(commandList);

        if (cwd != null) {
            builder.directory(new File(cwd));
        }

        Process process = builder.start();
        String stdout = readStream(process.getInputStream());
        String stderr = readStream(process.getErrorStream());

        process.waitFor();

        return Arrays.asList(stdout, stderr);
    }

    private String readStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    @Override
    public List<Problem> getProblems(String filepath) {
        List<Problem> problems = new ArrayList<>();
        if (filepath == null) {
            return problems;
        }

        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filepath));
        if (file == null || !file.exists()) {
            return problems;
        }

        try {
            IMarker[] markers = file.findMarkers(IMarker.SEVERITY_ERROR, true, IResource.DEPTH_ZERO);
            for (IMarker marker : markers) {
                int startLine = marker.getAttribute(IMarker.LINE_NUMBER, -1) - 1;
                int endLine = startLine; // Eclipse에서는 줄 단위로 marker를 제공함
                problems.add(new Problem(filepath, new Range(new Position(startLine, 0), new Position(endLine, 0)), marker.getAttribute(IMarker.MESSAGE, "")));
            }

            markers = file.findMarkers(IMarker.SEVERITY_WARNING, true, IResource.DEPTH_ZERO);
            for (IMarker marker : markers) {
                int startLine = marker.getAttribute(IMarker.LINE_NUMBER, -1) - 1;
                int endLine = startLine; // Eclipse에서는 줄 단위로 marker를 제공함
                problems.add(new Problem(filepath, new Range(new Position(startLine, 0), new Position(endLine, 0)), marker.getAttribute(IMarker.MESSAGE, "")));
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }

        return problems;
    }

    @Override
    public String getBranch(String dir) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("git", "rev-parse", "--abbrev-ref", "HEAD");
        builder.directory(new File(dir));

        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String output = reader.readLine();

        process.waitFor();

        return output != null ? output : "NONE";
    }

    @Override
    public List<IndexTag> getTags(String artifactId) throws ExecutionException, InterruptedException {
        List<String> workspaceDirs = Arrays.asList(workspaceDirectories());
        List<String> branches = new ArrayList<>();

        for (String dir : workspaceDirs) {
            branches.add(getBranch(dir));
        }

        List<IndexTag> result = new ArrayList<>();
        for (int i = 0; i < workspaceDirs.size(); i++) {
            result.add(new IndexTag(workspaceDirs.get(i), branches.get(i), artifactId));
        }

        return result;
    }

    @Override
    public String getRepoName(String dir) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("git", "config", "--get", "remote.origin.url");
        builder.directory(new File(dir));

        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String output = reader.readLine();

        if (output != null) {
            output = output.trim();
        }

        process.waitFor();

        return output;
    }

    @Override
    public Object showToast(ToastType type, String message, Object... otherParams) throws Exception {
        // Eclipse는 NotificationAction과 같은 기능이 없으므로 간단하게 MessageDialog를 사용
        Display.getDefault().asyncExec(() -> {
            switch (type.toLowerCase()) {
                case "error":
                    MessageDialog.openError(null, "Continue", message);
                    break;
                case "warning":
                    MessageDialog.openWarning(null, "Continue", message);
                    break;
                default:
                    MessageDialog.openInformation(null, "Continue", message);
                    break;
            }
        });

        // 예외를 처리하는 동작이 포함되어 있지 않으므로 모든 경우 빈 문자열 반환
        return "";
    }

    @Override
    public String getGitRootPath(String dir) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("git", "rev-parse", "--show-toplevel");
        builder.directory(new File(dir));

        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String output = reader.readLine();

        process.waitFor();

        return output;
    }

    @Override
    public List<List<String>> listDir(String dir) {
        File directory = new File(dir);
        if (!directory.isDirectory()) {
            return Collections.emptyList();
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }

        List<List<String>> result = new ArrayList<>();
        for (File file : files) {
            result.add(Arrays.asList(file.getName(), file.isDirectory() ? "DIRECTORY" : "FILE"));
        }

        return result;
    }

    @Override
    public Map<String, FileStats> getFileStats(List<String> files) throws IOException {
        Map<String, FileStats> fileStatsMap = new HashMap<>();
        for (String file : files) {
            FileStats fileStats = new FileStats(new File(file).lastModified(), new File(file).length());
            fileStatsMap.put(file, fileStats);
        }
        return fileStatsMap;
    }

    @Override
   	public String getGitHubAuthToken(GetGhTokenArgs args) throws Exception {
        return continuePluginService.getContinueExtensionSettings().getContinueState().getGhAuthToken();
    }

    @Override
   	public List<RangeInFile> gotoDefinition(Location location) throws Exception {
        throw new UnsupportedOperationException("gotoDefinition not implemented yet");
    }

    @Override
    public void onDidChangeActiveTextEditor(IdeCallback callback) {
        throw new UnsupportedOperationException("onDidChangeActiveTextEditor not implemented yet");
    }

    private void setFileOpen(String filepath, boolean open) {
        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filepath));
        if (file == null || !file.exists()) {
            return;
        }

        Display.getDefault().asyncExec(() -> {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            try {
                if (open) {
                    IDE.openEditor(page, file, true);
                } else {
                    page.closeEditor(page.findEditor(new FileEditorInput(file)), false);
                }
            } catch (PartInitException e) {
                e.printStackTrace();
            }
        });
    }

    private String[] workspaceDirectories() {
        String[] dirs = continuePluginService.getWorkspacePaths();
        if (dirs != null && dirs.length > 0) {
            return dirs;
        }

        return new String[]{project.getLocationURI().getPath()};
    }
}