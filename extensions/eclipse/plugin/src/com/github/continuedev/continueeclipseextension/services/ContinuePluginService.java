package com.github.continuedev.continueeclipseextension.services;

import com.github.continuedev.continueeclipseextension.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.ui.Messages;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ContinueExtensionSettingsService {

    private static final ContinueExtensionSettingsService INSTANCE = new ContinueExtensionSettingsService();

    private ContinueState continueState;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> remoteSyncFuture;

    private ContinueExtensionSettingsService() {
        continueState = new ContinueState();
    }

    public static ContinueExtensionSettingsService getInstance() {
        return INSTANCE;
    }

    public ContinueState getState() {
        return continueState;
    }

    public void loadState(ContinueState state) {
        this.continueState = state;
    }

    public void syncRemoteConfig() {
        String remoteConfigServerUrl = continueState.getRemoteConfigServerUrl();

        if (remoteConfigServerUrl != null && !remoteConfigServerUrl.isEmpty()) {
            OkHttpClient client = new OkHttpClient();
            String baseUrl = remoteConfigServerUrl.replaceAll("/$", "");

            Request.Builder requestBuilder = new Request.Builder().url(baseUrl + "/sync");

            String userToken = continueState.getUserToken();
            if (userToken != null) {
                requestBuilder.addHeader("Authorization", "Bearer " + userToken);
            }

            Request request = requestBuilder.build();
            ContinueRemoteConfigSyncResponse configResponse = null;

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseBody = response.body().string();
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    configResponse = objectMapper.readValue(responseBody, ContinueRemoteConfigSyncResponse.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (configResponse != null && configResponse.getConfigJson() != null && !configResponse.getConfigJson().isEmpty()) {
                File file = new File(Constants.getConfigJsonPath(request.url().host()));
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(configResponse.getConfigJson());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (configResponse != null && configResponse.getConfigJs() != null && !configResponse.getConfigJs().isEmpty()) {
                File file = new File(Constants.getConfigJsPath(request.url().host()));
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(configResponse.getConfigJs());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addRemoteSyncJob() {
        if (remoteSyncFuture != null) {
            remoteSyncFuture.cancel(false);
        }

        if (scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(1);
        }

        remoteSyncFuture = scheduler.scheduleWithFixedDelay(
                this::syncRemoteConfig,
                0,
                continueState.getRemoteConfigSyncPeriod(),
                TimeUnit.MINUTES
        );
    }

    @Serializable
    public static class ContinueRemoteConfigSyncResponse {
        private String configJson;
        private String configJs;

        public String getConfigJson() {
            return configJson;
        }

        public void setConfigJson(String configJson) {
            this.configJson = configJson;
        }

        public String getConfigJs() {
            return configJs;
        }

        public void setConfigJs(String configJs) {
            this.configJs = configJs;
        }
    }

    public static class ContinueState {
        private String lastSelectedInlineEditModel;
        private boolean shownWelcomeDialog;
        private String remoteConfigServerUrl;
        private int remoteConfigSyncPeriod = 60;
        private String userToken;
        private boolean enableTabAutocomplete = true;
        private String ghAuthToken;
        private boolean enableContinueTeamsBeta = false;
        private boolean enableOSR = shouldRenderOffScreen();
        private boolean displayEditorTooltip = true;
        private boolean showIDECompletionSideBySide = false;
        private String continueTestEnvironment = "production";

        public String getLastSelectedInlineEditModel() {
            return lastSelectedInlineEditModel;
        }

        public void setLastSelectedInlineEditModel(String lastSelectedInlineEditModel) {
            this.lastSelectedInlineEditModel = lastSelectedInlineEditModel;
        }

        public boolean isShownWelcomeDialog() {
            return shownWelcomeDialog;
        }

        public void setShownWelcomeDialog(boolean shownWelcomeDialog) {
            this.shownWelcomeDialog = shownWelcomeDialog;
        }

        public String getRemoteConfigServerUrl() {
            return remoteConfigServerUrl;
        }

        public void setRemoteConfigServerUrl(String remoteConfigServerUrl) {
            this.remoteConfigServerUrl = remoteConfigServerUrl;
        }

        public int getRemoteConfigSyncPeriod() {
            return remoteConfigSyncPeriod;
        }

        public void setRemoteConfigSyncPeriod(int remoteConfigSyncPeriod) {
            this.remoteConfigSyncPeriod = remoteConfigSyncPeriod;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

        public boolean isEnableTabAutocomplete() {
            return enableTabAutocomplete;
        }

        public void setEnableTabAutocomplete(boolean enableTabAutocomplete) {
            this.enableTabAutocomplete = enableTabAutocomplete;
        }

        public String getGhAuthToken() {
            return ghAuthToken;
        }

        public void setGhAuthToken(String ghAuthToken) {
            this.ghAuthToken = ghAuthToken;
        }

        public boolean isEnableContinueTeamsBeta() {
            return enableContinueTeamsBeta;
        }

        public void setEnableContinueTeamsBeta(boolean enableContinueTeamsBeta) {
            this.enableContinueTeamsBeta = enableContinueTeamsBeta;
        }

        public boolean isEnableOSR() {
            return enableOSR;
        }

        public void setEnableOSR(boolean enableOSR) {
            this.enableOSR = enableOSR;
        }

        public boolean isDisplayEditorTooltip() {
            return displayEditorTooltip;
        }

        public void setDisplayEditorTooltip(boolean displayEditorTooltip) {
            this.displayEditorTooltip = displayEditorTooltip;
        }

        public boolean isShowIDECompletionSideBySide() {
            return showIDECompletionSideBySide;
        }

        public void setShowIDECompletionSideBySide(boolean showIDECompletionSideBySide) {
            this.showIDECompletionSideBySide = showIDECompletionSideBySide;
        }

        public String getContinueTestEnvironment() {
            return continueTestEnvironment;
        }

        public void setContinueTestEnvironment(String continueTestEnvironment) {
            this.continueTestEnvironment = continueTestEnvironment;
        }
    }

    private static boolean shouldRenderOffScreen() {
        int minBuildNumber = 233;
        // Eclipse에서는 해당 정보를 얻는 방법이 다르므로, 예시와 같은 방법으로 대체합니다.
        // 예를 들어, Eclipse 버전 정보를 얻는 방법을 사용해야 합니다.
        // 현재 대체로 Eclipse의 버전 정보를 비교하는 방법은 OSGi 버전 정보를 이용하는 것입니다.
        // 하드 코딩된 예시를 사용합니다.
        return true; // Eclipse 버전 확인 로직 구현 필요
    }
}

class ContinueExtensionConfigurable extends PreferencePage implements IWorkbenchPreferencePage {
    private Text remoteConfigServerUrl;
    private Text remoteConfigSyncPeriod;
    private Text userToken;
    private Button enableTabAutocomplete;
    private Button enableContinueTeamsBeta;
    private Button enableOSR;
    private Button displayEditorTooltip;
    private Button showIDECompletionSideBySide;

    @Override
    protected Control createContents(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        container.setLayout(new GridLayout(2, false));

        createLabel(container, "Remote Config Server URL:");
        remoteConfigServerUrl = createText(container);

        createLabel(container, "Remote Config Sync Period (in minutes):");
        remoteConfigSyncPeriod = createText(container);

        createLabel(container, "User Token:");
        userToken = createText(container);

        enableTabAutocomplete = createCheckBox(container, "Enable Tab Autocomplete");
        enableContinueTeamsBeta = createCheckBox(container, "Enable Continue for Teams Beta");
        enableOSR = createCheckBox(container, "Enable Off-Screen Rendering");
        displayEditorTooltip = createCheckBox(container, "Display Editor Tooltip");
        showIDECompletionSideBySide = createCheckBox(container, "Show IDE completions side-by-side");

        loadSettings();
        return container;
    }

    private Label createLabel(Composite parent, String text) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(text);
        label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        return label;
    }

    private Text createText(Composite parent) {
        Text text = new Text(parent, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        return text;
    }

    private Button createCheckBox(Composite parent, String text) {
        Button checkBox = new Button(parent, SWT.CHECK);
        checkBox.setText(text);
        checkBox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        return checkBox;
    }

    private void loadSettings() {
        ContinueExtensionSettingsService.ContinueState state = ContinueExtensionSettingsService.getInstance().getState();
        this.remoteConfigServerUrl.setText(state.getRemoteConfigServerUrl());
        this.remoteConfigSyncPeriod.setText(String.valueOf(state.getRemoteConfigSyncPeriod()));
        this.userToken.setText(state.getUserToken());
        this.enableTabAutocomplete.setSelection(state.isEnableTabAutocomplete());
        this.enableContinueTeamsBeta.setSelection(state.isEnableContinueTeamsBeta());
        this.enableOSR.setSelection(state.isEnableOSR());
        this.displayEditorTooltip.setSelection(state.isDisplayEditorTooltip());
        this.showIDECompletionSideBySide.setSelection(state.isShowIDECompletionSideBySide());
    }

    @Override
    public boolean performOk() {
        saveSettings();
        ContinueExtensionSettingsService.getInstance().addRemoteSyncJob();
        return super.performOk();
    }

    @Override
    protected void performDefaults() {
        reset();
        super.performDefaults();
    }

    private void saveSettings() {
        ContinueExtensionSettingsService.ContinueState state = ContinueExtensionSettingsService.getInstance().getState();
        state.setRemoteConfigServerUrl(remoteConfigServerUrl.getText());
        try {
            state.setRemoteConfigSyncPeriod(Integer.parseInt(remoteConfigSyncPeriod.getText()));
        } catch (NumberFormatException e) {
            Messages.error("Invalid sync period", "Please enter a valid integer for sync period.", null);
            return;
        }
        state.setUserToken(userToken.getText());
        state.setEnableTabAutocomplete(enableTabAutocomplete.getSelection());
        state.setEnableContinueTeamsBeta(enableContinueTeamsBeta.getSelection());
        state.setEnableOSR(enableOSR.getSelection());
        state.setDisplayEditorTooltip(displayEditorTooltip.getSelection());
        state.setShowIDECompletionSideBySide(showIDECompletionSideBySide.getSelection());

        ContinueExtensionSettingsService.getInstance().loadState(state);
    }

    private void reset() {
        ContinueExtensionSettingsService.ContinueState defaultState = new ContinueExtensionSettingsService.ContinueState();
        remoteConfigServerUrl.setText(defaultState.getRemoteConfigServerUrl());
        remoteConfigSyncPeriod.setText(String.valueOf(defaultState.getRemoteConfigSyncPeriod()));
        userToken.setText(defaultState.getUserToken());
        enableTabAutocomplete.setSelection(defaultState.isEnableTabAutocomplete());
        enableContinueTeamsBeta.setSelection(defaultState.isEnableContinueTeamsBeta());
        enableOSR.setSelection(defaultState.isEnableOSR());
        displayEditorTooltip.setSelection(defaultState.isDisplayEditorTooltip());
        showIDECompletionSideBySide.setSelection(defaultState.isShowIDECompletionSideBySide());

        ContinueExtensionSettingsService.getInstance().loadState(defaultState);
    }

    @Override
    public void init(IWorkbench workbench) {
        // Initialization code if needed
    }

    @Override
    public void dispose() {
        // Dispose code if needed
    }
}