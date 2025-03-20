package com.github.continuedev.continueeclipseextension.activities;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IDisposable;

/**
 * The service is a parent disposable that represents the entire plugin lifecycle
 * and is intended to be used instead of the workbench as a parent disposable,
 * ensures that disposables registered using it as parents will be processed when the plugin is unloaded to avoid memory leaks.
 *
 * @author lk
 */
public class ContinuePluginDisposable implements IDisposable {

    @Override
    public void dispose() {
        // 서비스 해제 로직 작성
    }

    public static ContinuePluginDisposable getInstance() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench != null && !workbench.isClosing()) {
            return Platform.getService(ContinuePluginDisposable.class);
        }
        return null;
    }

    public static ContinuePluginDisposable getInstance(Object project) {
        // Eclipse에서 일정한 Project 구조체를 사용하므로 Java의 Object로 대체.
        // 프로젝트별 서비스의 경우 IProject를 사용할 수 있습니다.
        // 여기서는 간단히 객체를 반환하도록 처리합니다.
        return Platform.getService(ContinuePluginDisposable.class);
    }
}