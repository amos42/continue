package com.github.continuedev.continueeclipseextension.auth;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;

public interface AuthListener {
    void startAuthFlow();

    void handleUpdatedSessionInfo(ControlPlaneSessionInfo sessionInfo);

    class AuthListenerHelper {
        public static final String TOPIC_ID = "com.github.continuedev.continueintellijextension.auth.StartAuthFlow";

        public static org.eclipse.jface.util.ListenerList<AuthListener> getTopic() {
            // Eclipse에서 로컬로 ListenerList를 사용하거나,
            // 다른 방식으로 이벤트 메커니즘을 구현해야 합니다.
            return new org.eclipse.jface.util.ListenerList<>();
        }
    }
}