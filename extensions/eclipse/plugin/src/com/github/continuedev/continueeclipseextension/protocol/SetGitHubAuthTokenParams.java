package com.github.continuedev.continueeclipseextension.protocol;

public class SetGitHubAuthTokenParams {
    private final String token;

    public SetGitHubAuthTokenParams(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

