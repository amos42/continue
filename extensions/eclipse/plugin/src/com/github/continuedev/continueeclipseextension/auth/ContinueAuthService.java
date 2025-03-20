package com.github.continuedev.continueeclipseextension.auth;

import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.github.continuedev.continueeclipseextension.utils.Desktop;
import com.google.gson.Gson;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContinueAuthService {

    private static final String CREDENTIALS_USER = "ContinueAuthUser";
    private static final String ACCESS_TOKEN_KEY = "ContinueAccessToken";
    private static final String REFRESH_TOKEN_KEY = "ContinueRefreshToken";
    private static final String ACCOUNT_ID_KEY = "ContinueAccountId";
    private static final String ACCOUNT_LABEL_KEY = "ContinueAccountLabel";

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static ContinueAuthService getInstance() {
        return new ContinueAuthService();
    }

    private String getControlPlaneUrl() {
        ContinueExtensionSettings settings = ContinueExtensionSettings.getInstance();
        String env = settings.getContinueState().getContinueTestEnvironment();
        switch (env) {
            case "none":
                return "https://control-plane-api-service-i3dqylpbqa-uc.a.run.app";
            case "local":
                return "http://localhost:3001";
            case "production":
                return "https://api.continue.dev";
            case "test":
                return "https://api-test.continue.dev";
        }
        return "https://control-plane-api-service-i3dqylpbqa-uc.a.run.app";
    }

    public ContinueAuthService() {
        ContinueExtensionSettings settings = ContinueExtensionSettings.getInstance();
        if (settings.getContinueState().isEnableContinueTeamsBeta()) {
            setupRefreshTokenInterval();
        }
    }

    public void startAuthFlow(Shell shell, boolean useOnboarding) {
        openSignInPage(shell.getDisplay(), useOnboarding);
        Display.getDefault().asyncExec(() -> {
            Dialog dialog = new ContinueAuthDialog(shell, useOnboarding, token -> {
                updateRefreshToken(token);
            });
            dialog.open();
        });
    }

    public void signOut() {
        setAccessToken("");
        setRefreshToken("");
        setAccountId("");
        setAccountLabel("");
    }

    private void updateRefreshToken(String token) {
        scheduler.execute(() -> {
            try {
                Map<String, Object> response = refreshToken(token);
                String accessToken = (String) response.get("accessToken");
                String refreshToken = (String) response.get("refreshToken");

                Map<String, Object> user = (Map<String, Object>) response.get("user");
                String firstName = (String) user.get("firstName");
                String lastName = (String) user.get("lastName");
                String label = firstName + " " + lastName;
                String id = (String) user.get("id");
                String email = (String) user.get("email");

                setRefreshToken(refreshToken);
                ControlPlaneSessionInfo sessionInfo = new ControlPlaneSessionInfo(accessToken,
                        new ControlPlaneSessionInfo.Account(email, label));
                setControlPlaneSessionInfo(sessionInfo);

            } catch (Exception e) {
                System.out.println("Exception while refreshing token: " + e.getMessage());
            }
        });
    }

    private void setupRefreshTokenInterval() {
        scheduler.scheduleWithFixedDelay(() -> {
            String refreshToken = getRefreshToken();
            if (refreshToken != null) {
                updateRefreshToken(refreshToken);
            }
        }, 15 * 60, 15 * 60, TimeUnit.SECONDS);
    }

    private Map<String, Object> refreshToken(String refreshToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        URL url = new URL(new URI(getControlPlaneUrl()).resolve("/auth/refresh").toString());
        Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("refreshToken", refreshToken);
        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonBody);
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, Map.class);
        }
    }

    private void openSignInPage(Display display, boolean useOnboarding) {
        ContinuePluginService pluginService = ContinuePluginService.getInstance();
        pluginService.getCoreMessenger().request(
                "auth/getAuthUrl", Map.of("useOnboarding", useOnboarding), null,
                response -> {
                    Map<String, Object> content = (Map<String, Object>) ((Map<String, Object>) response).get("content");
                    String authUrl = (String) content.get("url");
                    if (authUrl != null) {
                        Desktop.browse(URI.create(authUrl));
                    }
                });
    }

    private String retrieveSecret(String key) {
        IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode("com.github.continuedev.continueueclipseextension");
        try {
            return prefs.get(key, null);
        } catch (Exception e) {
            System.out.println("Error retrieving secret for key " + key + ": " + e.getMessage());
            return null;
        }
    }

    private void storeSecret(String key, String secret) {
        IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode("com.github.continuedev.continueueclipseextension");
        try {
            prefs.put(key, secret);
            prefs.flush();
        } catch (Exception e) {
            System.out.println("Error storing secret for key " + key + ": " + e.getMessage());
        }
    }

    private String getAccessToken() {
        return retrieveSecret(ACCESS_TOKEN_KEY);
    }

    private void setAccessToken(String token) {
        storeSecret(ACCESS_TOKEN_KEY, token);
    }

    private String getRefreshToken() {
        return retrieveSecret(REFRESH_TOKEN_KEY);
    }

    private void setRefreshToken(String token) {
        storeSecret(REFRESH_TOKEN_KEY, token);
    }

    public String getAccountId() {
        IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode("com.github.continuedev.continueueclipseextension");
        return prefs.get(ACCOUNT_ID_KEY, null);
    }

    public void setAccountId(String id) {
        IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode("com.github.continuedev.continueueclipseextension");
        prefs.put(ACCOUNT_ID_KEY, id);
    }

    public String getAccountLabel() {
        IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode("com.github.continuedev.continueueclipseextension");
        return prefs.get(ACCOUNT_LABEL_KEY, null);
    }

    public void setAccountLabel(String label) {
        IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode("com.github.continuedev.continueueclipseextension");
        prefs.put(ACCOUNT_LABEL_KEY, label);
    }

    public ControlPlaneSessionInfo loadControlPlaneSessionInfo() {
        String accessToken = getAccessToken();
        String accountId = getAccountId();
        String accountLabel = getAccountLabel();

        if (accessToken != null && !accessToken.isEmpty() && accountId != null && accountLabel != null) {
            return new ControlPlaneSessionInfo(accessToken,
                    new ControlPlaneSessionInfo.Account(accountId, accountLabel));
        }
        return null;
    }

    public void setControlPlaneSessionInfo(ControlPlaneSessionInfo info) {
        setAccessToken(info.getAccessToken());
        setAccountId(info.getAccount().getId());
        setAccountLabel(info.getAccount().getLabel());
    }

    // 데이터 클래스 대신 일반 클래스로 변경
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
}