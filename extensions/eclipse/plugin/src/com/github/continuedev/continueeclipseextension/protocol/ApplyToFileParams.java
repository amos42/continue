package com.github.continuedev.continueeclipseextension.protocol;

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

