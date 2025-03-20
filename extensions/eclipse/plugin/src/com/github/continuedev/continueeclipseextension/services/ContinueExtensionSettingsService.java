package com.github.continuedev.continueeclipseextension.services;

import com.github.continuedev.continueeclipseextension.continuue.CoreMessenger;
import com.github.continuedev.continueeclipseextension.continuue.CoreMessengerManager;
import com.github.continuedev.continueeclipseextension.continuue.DiffManager;
import com.github.continuedev.continueeclipseextension.continuue.IdeProtocolClient;
import com.github.continuedev.continueeclipseextension.toolWindow.ContinuePluginToolWindowFactory;
import com.github.continuedev.continueeclipseextension.toolWindow.ContinuePluginToolWindowFactory.ContinuePluginWindow;
import com.github.continuedev.continueeclipseextension.utils.Utils;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.NonRule;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.IWorkbenchWindow;
import org.osgi.framework.BundleContext;

import java.util.UUID;

public class ContinuePluginService implements org.eclipse.ui.services.IDisposable, org.eclipse.ui.services.IDumbAware {
    private Job debouncingJob;
    private ContinuePluginWindow continuePluginWindow;
    private IdeProtocolClient ideProtocolClient;
    private CoreMessengerManager coreMessengerManager;
    private String[] workspacePaths;
    private String windowId = Utils.uuid();
    private DiffManager diffManager;

    public ContinuePluginService() {
        coreMessengerManager = new CoreMessengerManager();
        ideProtocolClient = new IdeProtocolClient();
        diffManager = new DiffManager();
        workspacePaths = new String[]{getWorkspacePath()};
    }

    @Override
    public void dispose() {
        if (debouncingJob != null) {
            debouncingJob.cancel();
        }

        if (coreMessengerManager != null && coreMessengerManager.getCoreMessenger() != null) {
            coreMessengerManager.getCoreMessenger().getCoroutineScope().cancel();
            coreMessengerManager.getCoreMessenger().killSubProcess();
        }
    }

    public void sendToWebview(String messageType, Object data, String messageId) {
        if (continuePluginWindow != null && continuePluginWindow.getBrowser() != null) {
            continuePluginWindow.getBrowser().sendToWebview(messageType, data, messageId);
        }
    }

    public void sendToWebview(String messageType, Object data) {
        sendToWebview(messageType, data, Utils.uuid());
    }

    private String getWorkspacePath() {
        // Eclipse의 Workspace 경로를 반환합니다.
        return AbstractUIPlugin.getPluginPreferences().getString("workspace_path");
    }

    public CoreMessenger getCoreMessenger() {
        if (coreMessengerManager != null) {
            return coreMessengerManager.getCoreMessenger();
        }
        return null;
    }

    public void setContinuePluginWindow(ContinuePluginWindow continuePluginWindow) {
        this.continuePluginWindow = continuePluginWindow;
    }

    public void setWorkspacePaths(String[] workspacePaths) {
        this.workspacePaths = workspacePaths;
    }

    public String getWindowId() {
        return windowId;
    }

    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }

    public IdeProtocolClient getIdeProtocolClient() {
        return ideProtocolClient;
    }

    public void setIdeProtocolClient(IdeProtocolClient ideProtocolClient) {
        this.ideProtocolClient = ideProtocolClient;
    }

    public CoreMessengerManager getCoreMessengerManager() {
        return coreMessengerManager;
    }

    public void setCoreMessengerManager(CoreMessengerManager coreMessengerManager) {
        this.coreMessengerManager = coreMessengerManager;
    }

    public String[] getWorkspacePaths() {
        return workspacePaths;
    }

    public DiffManager getDiffManager() {
        return diffManager;
    }

    public void setDiffManager(DiffManager diffManager) {
        this.diffManager = diffManager;
    }

    // 예시로.debouncingJob을 생성하는 메서드 추가
    public void performActionWithDebounce(Runnable action, long delay, TimeUnit unit) {
        if (debouncingJob != null) {
            debouncingJob.cancel();
        }

        debouncingJob = new Job("Continue Plugin Debouncing Job") {
            @Override
            protected org.eclipse.core.runtime.IStatus run(org.eclipse.core.runtime.IProgressMonitor monitor) {
                action.run();
                return org.eclipse.core.runtime.Status.OK_STATUS;
            }
        };

        debouncingJob.setRule(NonRule.INSTANCE);
        debouncingJob.schedule((long) (delay * unit.toMillis(1)));
    }

    // OSGi BundleContext를 초기화하는 메서드 추가
    @Override
    public void initialize(org.eclipse.ui.services.IServiceLocator serviceLocator) {
        // 초기화 로직 추가 (예: ToolWindow 생성 등)
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        continuePluginWindow = new ContinuePluginToolWindowFactory.ContinuePluginWindow(window);
    }

    // 활성화 메서드 추가
    public void start(BundleContext context) throws Exception {
        super.start(context);
        // 필요한 초기화 작업 수행
        performActionWithDebounce(() -> {
            // 초기 작업
        }, 1, TimeUnit.SECONDS);
    }

    // 비활성화 메서드 추가
    public void stop(BundleContext context) throws Exception {
        dispose();
        super.stop(context);
    }
}
