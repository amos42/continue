package com.github.continuedev.continueeclipseextension;

import com.google.gson.JsonElement;

public class Types {

    public enum IdeType {
        JETBRAINS("jetbrains"),
        VSCODE("vscode");

        private final String value;
        IdeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ToastType {
        INFO("info"),
        ERROR("error"),
        WARNING("warning");

        private final String value;
        ToastType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum FileType {
        UNKNOWN(0),
        FILE(1),
        DIRECTORY(2),
        SYMBOLIC_LINK(64);

        private final int value;
        FileType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ConfigMergeType {
        MERGE,
        OVERWRITE
    }

    public static class Position {
        private final int line;
        private final int character;

        public Position(int line, int character) {
            this.line = line;
            this.character = character;
        }

        public int getLine() {
            return line;
        }

        public int getCharacter() {
            return character;
        }
    }

    public static class Range {
        private final Position start;
        private final Position end;

        public Range(Position start, Position end) {
            this.start = start;
            this.end = end;
        }

        public Position getStart() {
            return start;
        }

        public Position getEnd() {
            return end;
        }
    }

    public static class IdeInfo {
        private final IdeType ideType;
        private final String name;
        private final String version;
        private final String remoteName;
        private final String extensionVersion;

        public IdeInfo(IdeType ideType, String name, String version, String remoteName, String extensionVersion) {
            this.ideType = ideType;
            this.name = name;
            this.version = version;
            this.remoteName = remoteName;
            this.extensionVersion = extensionVersion;
        }

        public IdeType getIdeType() {
            return ideType;
        }

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public String getRemoteName() {
            return remoteName;
        }

        public String getExtensionVersion() {
            return extensionVersion;
        }
    }

    public static class Problem {
        private final String filepath;
        private final Range range;
        private final String message;

        public Problem(String filepath, Range range, String message) {
            this.filepath = filepath;
            this.range = range;
            this.message = message;
        }

        public String getFilepath() {
            return filepath;
        }

        public Range getRange() {
            return range;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class Thread {
        private final String name;
        private final int id;

        public Thread(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

    public static class IndexTag {
        private final String artifactId;
        private final String branch;
        private final String directory;

        public IndexTag(String artifactId, String branch, String directory) {
            this.artifactId = artifactId;
            this.branch = branch;
            this.directory = directory;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public String getBranch() {
            return branch;
        }

        public String getDirectory() {
            return directory;
        }
    }

    public static class Location {
        private final String filepath;
        private final Position position;

        public Location(String filepath, Position position) {
            this.filepath = filepath;
            this.position = position;
        }

        public String getFilepath() {
            return filepath;
        }

        public Position getPosition() {
            return position;
        }
    }

    public static class RangeInFile {
        private final String filepath;
        private final Range range;

        public RangeInFile(String filepath, Range range) {
            this.filepath = filepath;
            this.range = range;
        }

        public String getFilepath() {
            return filepath;
        }

        public Range getRange() {
            return range;
        }
    }

    public static class RangeInFileWithContents {
        private final String filepath;
        private final Range range;
        private final String contents;

        public RangeInFileWithContents(String filepath, Range range, String contents) {
            this.filepath = filepath;
            this.range = range;
            this.contents = contents;
        }

        public String getFilepath() {
            return filepath;
        }

        public Range getRange() {
            return range;
        }

        public String getContents() {
            return contents;
        }
    }

    public static class ControlPlaneSessionInfo {
        private final String accessToken;
        private final Account account;

        public ControlPlaneSessionInfo(String accessToken, Account account) {
            this.accessToken = accessToken;
            this.account = account;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public Account getAccount() {
            return account;
        }
    }

    public static class Account {
        private final String label;
        private final String id;

        public Account(String label, String id) {
            this.label = label;
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public String getId() {
            return id;
        }
    }

    public static class FileStats {
        private final long lastModified;
        private final long size;

        public FileStats(long lastModified, long size) {
            this.lastModified = lastModified;
            this.size = size;
        }

        public long getLastModified() {
            return lastModified;
        }

        public long getSize() {
            return size;
        }
    }

    public static class IdeSettings {
        private final String remoteConfigServerUrl;
        private final int remoteConfigSyncPeriod;
        private final String userToken;
        private final boolean enableControlServerBeta;
        private final boolean pauseCodebaseIndexOnStart;
        private final String continueTestEnvironment;

        public IdeSettings(String remoteConfigServerUrl, int remoteConfigSyncPeriod, String userToken, boolean enableControlServerBeta, boolean pauseCodebaseIndexOnStart, String continueTestEnvironment) {
            this.remoteConfigServerUrl = remoteConfigServerUrl;
            this.remoteConfigSyncPeriod = remoteConfigSyncPeriod;
            this.userToken = userToken;
            this.enableControlServerBeta = enableControlServerBeta;
            this.pauseCodebaseIndexOnStart = pauseCodebaseIndexOnStart;
            this.continueTestEnvironment = continueTestEnvironment;
        }

        public String getRemoteConfigServerUrl() {
            return remoteConfigServerUrl;
        }

        public int getRemoteConfigSyncPeriod() {
            return remoteConfigSyncPeriod;
        }

        public String getUserToken() {
            return userToken;
        }

        public boolean isEnableControlServerBeta() {
            return enableControlServerBeta;
        }

        public boolean isPauseCodebaseIndexOnStart() {
            return pauseCodebaseIndexOnStart;
        }

        public String getContinueTestEnvironment() {
            return continueTestEnvironment;
        }
    }

    public static class ContinueRcJson {
        private final ConfigMergeType mergeBehavior;

        public ContinueRcJson(ConfigMergeType mergeBehavior) {
            this.mergeBehavior = mergeBehavior;
        }

        public ConfigMergeType getMergeBehavior() {
            return mergeBehavior;
        }
    }

    public interface IDE {
        IdeInfo getIdeInfo() throws Exception;

        IdeSettings getIdeSettings() throws Exception;

        List<String> getDiff(boolean includeUnstaged) throws Exception;

        Map<String, String> getClipboardContent() throws Exception;

        boolean isTelemetryEnabled() throws Exception;

        String getUniqueId() throws Exception;

        String getTerminalContents() throws Exception;

        String getDebugLocals(int threadIndex) throws Exception;

        List<String> getTopLevelCallStackSources(int threadIndex, int stackDepth) throws Exception;

        List<Thread> getAvailableThreads() throws Exception;

        List<String> getWorkspaceDirs() throws Exception;

        List<ContinueRcJson> getWorkspaceConfigs() throws Exception;

        boolean fileExists(String filepath) throws Exception;

        void writeFile(String path, String contents) throws Exception;

        void showVirtualFile(String title, String contents) throws Exception;

        String getContinueDir() throws Exception;

        void openFile(String path) throws Exception;

        void openUrl(String url) throws Exception;

        void runCommand(String command) throws Exception;

        void saveFile(String filepath) throws Exception;

        String readFile(String filepath) throws Exception;

        String readRangeInFile(String filepath, Range range) throws Exception;

        void showLines(String filepath, int startLine, int endLine) throws Exception;

        void showDiff(String filepath, String newContents, int stepIndex) throws Exception;

        List<String> getOpenFiles() throws Exception;

        Map<String, Object> getCurrentFile() throws Exception;

        List<String> getPinnedFiles() throws Exception;

        String getSearchResults(String query) throws Exception;

        List<Object> subprocess(String command, String cwd) throws Exception;

        List<Problem> getProblems(String filepath) throws Exception;

        String getBranch(String dir) throws Exception;

        List<IndexTag> getTags(String artifactId) throws Exception;

        String getRepoName(String dir) throws Exception;

        Object showToast(ToastType type, String message, Object... otherParams) throws Exception;

        String getGitRootPath(String dir) throws Exception;

        List<List<Object>> listDir(String dir) throws Exception;

        Map<String, FileStats> getFileStats(List<String> files) throws Exception;

        String getGitHubAuthToken(GetGhTokenArgs args) throws Exception;

        List<RangeInFile> gotoDefinition(Location location) throws Exception;

        void onDidChangeActiveTextEditor(IdeCallback callback);
    }

    public static class GetGhTokenArgs {
        private final String force;

        public GetGhTokenArgs(String force) {
            this.force = force;
        }

        public String getForce() {
            return force;
        }
    }

    public static class Message {
        private final String messageType;
        private final String messageId;
        private final JsonElement data;

        public Message(String messageType, String messageId, JsonElement data) {
            this.messageType = messageType;
            this.messageId = messageId;
            this.data = data;
        }

        public String getMessageType() {
            return messageType;
        }

        public String getMessageId() {
            return messageId;
        }

        public JsonElement getData() {
            return data;
        }
    }

    // TODO: Needs to be updated to handle new "apply" logic
    public static class AcceptRejectDiff {
        private final boolean accepted;
        private final int stepIndex;

        public AcceptRejectDiff(boolean accepted, int stepIndex) {
            this.accepted = accepted;
            this.stepIndex = stepIndex;
        }

        public boolean isAccepted() {
            return accepted;
        }

        public int getStepIndex() {
            return stepIndex;
        }
    }

    public static class DeleteAtIndex {
        private final int index;

        public DeleteAtIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public interface IdeCallback {
        void execute(String filepath);
    }
}
