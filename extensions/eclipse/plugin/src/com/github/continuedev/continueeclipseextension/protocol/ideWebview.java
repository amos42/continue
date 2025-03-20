package com.github.continuedev.continueeclipseextension.protocol;

public class CopyTextParams {
    private final String text;

    public CopyTextParams(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

public class SetGitHubAuthTokenParams {
    private final String token;

    public SetGitHubAuthTokenParams(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

public class ApplyToFileParams {
    private final String text;
    private final String streamId;
    private final String curSelectedModelTitle;
    private final String filepath;

    public ApplyToFileParams(String text, String streamId, String curSelectedModelTitle, String filepath) {
        this.text = text;
        this.streamId = streamId;
        this.curSelectedModelTitle = curSelectedModelTitle;
        this.filepath = filepath;
    }

    public String getText() {
        return text;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getCurSelectedModelTitle() {
        return curSelectedModelTitle;
    }

    public String getFilepath() {
        return filepath;
    }
}

public class InsertAtCursorParams {
    private final String text;

    public InsertAtCursorParams(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}