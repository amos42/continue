package com.github.continuedev.continueeclipseextension.services;

import com.posthog.java.PostHog;
import com.posthog.java.PostHog.Builder;

import java.util.Map;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class TelemetryService {
    private static final String POSTHOG_API_KEY = "phc_JS6XFROuNbhJtVCEdTSYk6gl5ArRrTNMpCcguAXlSPs";
    private PostHog posthog;
    private String distinctId;

    public TelemetryService() {
        // 생성자에서 초기화할 내용이 없다면 비워둡니다.
    }

    public void setup(String distinctId) {
        this.posthog = new Builder(POSTHOG_API_KEY).host("https://app.posthog.com").build();
        this.distinctId = distinctId;
    }

    public void capture(String eventName, Map<String, Object> properties) {
        if (this.posthog == null || this.distinctId == null) {
            return;
        }
        try {
            this.posthog.capture(this.distinctId, eventName, properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (this.posthog != null) {
            this.posthog.shutdown();
        }
    }

    // OSGi BundleContext를 초기화하는 메서드 추가
    public void start(BundleContext context) throws Exception {
        // 초기화 로직 추가 (예: distinctId 설정 등)
        String distinctId = "unique_distinct_id"; // 실제 distinctId 설정 로직 구현 필요
        setup(distinctId);
    }

    // 비활성화 메서드 추가
    public void stop(BundleContext context) throws Exception {
        shutdown();
    }
}