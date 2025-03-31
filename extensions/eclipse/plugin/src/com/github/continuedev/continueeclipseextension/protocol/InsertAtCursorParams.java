package com.github.continuedev.continueeclipseextension.protocol;

public class InsertAtCursorParams {
    private final String text;

    public InsertAtCursorParams(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}