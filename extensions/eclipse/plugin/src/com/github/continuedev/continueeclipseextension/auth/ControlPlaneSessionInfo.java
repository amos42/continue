package com.github.continuedev.continueeclipseextension.auth;

public class ControlPlaneSessionInfo {
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

    public static class Account {
        private final String id;
        private final String label;

        public Account(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }
    }
}
