package com.github.continuedev.continueeclipseextension.services;

import com.github.continuedev.continueeclipseextension.constants.PluginConstants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ContinueExtensionSettingsService extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    private StringFieldEditor remoteConfigServerUrl;
    private StringFieldEditor remoteConfigSyncPeriod;
    private StringFieldEditor userToken;
    private Button enableTabAutocomplete;
    private Button enableContinueTeamsBeta;
    private Button enableOSR;
    private Button displayEditorTooltip;
    private Button showIDECompletionSideBySide;

    private ScheduledFuture<?> remoteSyncFuture;
    private ScheduledExecutorService scheduler;

    public ContinueExtensionSettingsService() {
        super(GRID);
        setPreferenceStore(ActivatePlugin.getDefault().getPreferenceStore());
        setDescription("계속 엑스텐션 설정을 관리합니다.");
    }

    @Override
    protected void createFieldEditors() {
        Composite parent = getFieldEditorParent();
        remoteConfigServerUrl = new StringFieldEditor(PluginConstants.REMOTE_CONFIG_SERVER_URL, "원격 설정 서버 URL:", parent);
        remoteConfigSyncPeriod = new StringFieldEditor(PluginConstants.REMOTE_CONFIG_SYNC_PERIOD, "원격 설정 동기화 주기 (분):", parent);
        userToken = new StringFieldEditor(PluginConstants.USER_TOKEN, "사용자 토큰:", parent);
        enableTabAutocomplete = new Button(parent, org.eclipse.swt.SWT.CHECK);
        enableTabAutocomplete.setText("탭 자동완성을 활성화");
        addField(new CheckboxPreferenceEditor(PluginConstants.ENABLE_TAB_AUTOCOMPLETE, enableTabAutocomplete));
        enableContinueTeamsBeta = new Button(parent, org.eclipse.swt.SWT.CHECK);
        enableContinueTeamsBeta.setText("컨티뉴 팀즈 베타를 활성화");
        addField(new CheckboxPreferenceEditor(PluginConstants.ENABLE_CONTINUE_TEAMS_BETA, enableContinueTeamsBeta));
        enableOSR = new Button(parent, org.eclipse.swt.SWT.CHECK);
        enableOSR.setText("오프스크린 렌더링 활성화");
        addField(new CheckboxPreferenceEditor(PluginConstants.ENABLE_OSR, enableOSR));
        displayEditorTooltip = new Button(parent, org.eclipse.swt.SWT.CHECK);
        displayEditorTooltip.setText("에디터 툴팁 표시");
        addField(new CheckboxPreferenceEditor(PluginConstants.DISPLAY_EDITOR_TOOLTIP, displayEditorTooltip));
        showIDECompletionSideBySide = new Button(parent, org.eclipse.swt.SWT.CHECK);
        showIDECompletionSideBySide.setText("IDE 완성도도를 옆으로 표시");
        addField(new CheckboxPreferenceEditor(PluginConstants.SHOW_IDE_COMPLETION_SIDE_BY_SIDE, showIDECompletionSideBySide));
    }

    @Override
    public void init(IWorkbench workbench) {
        scheduler = Executors.newScheduledThreadPool(1);
        addRemoteSyncJob();
    }

    @Override
    public boolean performOk() {
        boolean result = super.performOk();
        if (result) {
            // 설정 변경에 따른 리스너 호출 및 스케줄 작업 업데이트
            sendSettingsUpdatedEvent();
            resetSyncJob();
        }
        return result;
    }

    @Override
    protected void performDefaults() {
        super.performDefaults();
        // 기본값 설정 및 스케줄 작업 업데이트
        sendSettingsUpdatedEvent();
        resetSyncJob();
    }

    private void sendSettingsUpdatedEvent() {
        // Eclipse에서는 일반적으로 IEventBroker를 사용합니다.
        // 일단, 해당 기능은 별도의 구현이 필요합니다.
        // 여기서는 단순히 메시지를 보내는 것만 구현했습니다.
        // IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
        // eventBroker.send("settingsUpdated", getContinueState());
    }

    private ContinueState getContinueState() {
        ContinueState state = new ContinueState();
        state.remoteConfigServerUrl = remoteConfigServerUrl.getStringValue();
        state.remoteConfigSyncPeriod = Integer.parseInt(remoteConfigSyncPeriod.getStringValue());
        state.userToken = userToken.getStringValue();
        state.enableTabAutocomplete = enableTabAutocomplete.getSelection();
        state.enableContinueTeamsBeta = enableContinueTeamsBeta.getSelection();
        state.enableOSR = enableOSR.getSelection();
        state.displayEditorTooltip = displayEditorTooltip.getSelection();
        state.showIDECompletionSideBySide = showIDECompletionSideBySide.getSelection();
        return state;
    }

    private void resetSyncJob() {
        if (remoteSyncFuture != null) {
            remoteSyncFuture.cancel(false);
        }
        addRemoteSyncJob();
    }

    private void addRemoteSyncJob() {
        ContinueState state = getContinueState();
        if (state.remoteConfigServerUrl != null && !state.remoteConfigServerUrl.isEmpty()) {
            remoteSyncFuture = scheduler.scheduleWithFixedDelay(() -> syncRemoteConfig(state), 0,
                    state.remoteConfigSyncPeriod, TimeUnit.MINUTES);
        }
    }

    private void syncRemoteConfig(ContinueState state) {
        OkHttpClient client = new OkHttpClient();
        String baseUrl = state.remoteConfigServerUrl.trim().replaceAll("/$", "");

        Request.Builder requestBuilder = new Request.Builder().url(baseUrl + "/sync");

        if (state.userToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + state.userToken);
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("서버에서 성공적인 응답을 받지 못했습니다.");

            String responseBody = response.body().string();
            ContinueRemoteConfigSyncResponse configResponse;
            try {
                configResponse = new Gson().fromJson(responseBody, ContinueRemoteConfigSyncResponse.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return;
            }

            if (configResponse.configJson != null && !configResponse.configJson.isEmpty()) {
                File file = new File(PluginConstants.getConfigJsonPath(request.url.host()));
                file.writeText(configResponse.configJson);
            }

            if (configResponse.configJs != null && !configResponse.configJs.isEmpty()) {
                File file = new File(PluginConstants.getConfigJsPath(request.url.host()));
                file.writeText(configResponse.configJs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean shouldRenderOffScreen() {
        int minBuildNumber = 233;
        return Integer.parseInt(org.eclipse.ui.PlatformUI.getWorkbench().getVersion()) >= minBuildNumber;
    }

    @Override
    public void dispose() {
        super.dispose();
        remoteSyncFuture.cancel(false);
        scheduler.shutdown();
    }

    private static class ContinueState {
        String lastSelectedInlineEditModel;
        boolean shownWelcomeDialog;
        String remoteConfigServerUrl;
        int remoteConfigSyncPeriod = 60;
        String userToken;
        boolean enableTabAutocomplete = true;
        String ghAuthToken;
        boolean enableContinueTeamsBeta = false;
        boolean enableOSR = shouldRenderOffScreen();
        boolean displayEditorTooltip = true;
        boolean showIDECompletionSideBySide = false;
        String continueTestEnvironment = "production";

        private static boolean shouldRenderOffScreen() {
            int minBuildNumber = 233;
            // Eclipse 버전을 기준으로 판단하는 로직이 필요합니다.
            // 아래는 가상의 버전 추출 로직입니다.
            return Integer.parseInt(org.eclipse.ui.PlatformUI.getWorkbench().getVersion()) >= minBuildNumber;
        }
    }

    private static class ContinueRemoteConfigSyncResponse {
        String configJson;
        String configJs;
    }

    private static class CheckboxPreferenceEditor extends org.eclipse.jface.preference.BooleanFieldEditor {
        private Button button;

        CheckboxPreferenceEditor(String name, Button button) {
            super(name, name, button.getParent());
            this.button = button;
        }

        @Override
        protected void doLoad() {
            if (button != null) {
                button.setSelection(preferences.getBoolean(getPreferenceName(), false));
            }
        }

        @Override
        protected void doLoadDefault() {
            if (button != null) {
                button.setSelection(getPreferenceStore().getDefaultBoolean(getPreferenceName()));
            }
        }

        @Override
        protected void doStore() {
            if (button != null) {
                getPreferenceStore().setValue(getPreferenceName(), button.getSelection());
            }
        }
    }
}