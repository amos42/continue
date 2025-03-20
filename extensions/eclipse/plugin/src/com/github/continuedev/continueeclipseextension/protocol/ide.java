package com.github.continuedev.continueeclipseextension.protocol;

import com.github.continuedev.continueeclipseextension.Range;
import java.util.List;

public class GetControlPlaneSessionInfoParams {
    private final boolean silent;
    private final boolean useOnboarding;

    public GetControlPlaneSessionInfoParams(boolean silent, boolean useOnboarding) {
        this.silent = silent;
        this.useOnboarding = useOnboarding;
    }

    public boolean isSilent() {
        return silent;
    }

    public boolean isUseOnboarding() {
        return useOnboarding;
    }
}

public class WriteFileParams {
    private final String path;
    private final String contents;

    public WriteFileParams(String path, String contents) {
        this.path = path;
        this.contents = contents;
    }

    public String getPath() {
        return path;
    }

    public String getContents() {
        return contents;
    }
}

public class ShowVirtualFileParams {
    private final String name;
    private final String content;

    public ShowVirtualFileParams(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}

public class OpenFileParams {
    private final String path;

    public OpenFileParams(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

public class OpenUrlParam { 
    private final String url;

    public OpenUrlParam(String url) {
        this.url = url;
    }

    public String getValue() {
        return url;
    }
}

public class GetTagsParams { 
    private final String param;

    public GetTagsParams(String param) {
        this.param = param;
    }

    public String getValue() {
        return param;
    }
}

public class GetSearchResultsParams {
    private final String query;

    public GetSearchResultsParams(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}

public class SaveFileParams {
    private final String filepath;

    public SaveFileParams(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }
}

public class FileExistsParams {
    private final String filepath;

    public FileExistsParams(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }
}

public class ReadFileParams {
    private final String filepath;

    public ReadFileParams(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }
}

public class ShowDiffParams {
    private final String filepath;
    private final String newContents;
    private final int stepIndex;

    public ShowDiffParams(String filepath, String newContents, int stepIndex) {
        this.filepath = filepath;
        this.newContents = newContents;
        this.stepIndex = stepIndex;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getNewContents() {
        return newContents;
    }

    public int getStepIndex() {
        return stepIndex;
    }
}

public class ShowLinesParams {
    private final String filepath;
    private final int startLine;
    private final int endLine;

    public ShowLinesParams(String filepath, int startLine, int endLine) {
        this.filepath = filepath;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public String getFilepath() {
        return filepath;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }
}

public class ReadRangeInFileParams {
    private final String filepath;
    private final Range range;

    public ReadRangeInFileParams(String filepath, Range range) {
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

public class GetDiffParams {
    private final boolean includeUnstaged;

    public GetDiffParams(boolean includeUnstaged) {
        this.includeUnstaged = includeUnstaged;
    }

    public boolean isIncludeUnstaged() {
        return includeUnstaged;
    }
}

public class GetBranchParams {
    private final String dir;

    public GetBranchParams(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }
}

public class GetRepoNameParams {
    private final String dir;

    public GetRepoNameParams(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }
}

public class GetGitRootPathParams {
    private final String dir;

    public GetGitRootPathParams(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }
}

public class ListDirParams {
    private final String dir;

    public ListDirParams(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }
}

public class GetFileStatsParams {
    private final List<String> files;

    public GetFileStatsParams(List<String> files) {
        this.files = files;
    }

    public List<String> getFiles() {
        return files;
    }
}
