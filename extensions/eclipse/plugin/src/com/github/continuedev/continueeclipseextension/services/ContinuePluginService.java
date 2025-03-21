package com.github.continuedev.continueeclipseextension.services;

import com.github.continuedev.continueeclipseextension.CoreMessenger;
import com.github.continuedev.continueeclipseextension.CoreMessengerManager;
import com.github.continuedev.continueeclipseextension.DiffManager;
import com.github.continuedev.continueeclipseextension.IdeProtocolClient;
import com.github.continuedev.continueeclipseextension.utils.Utils;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.ui.part.ViewPart;

public class ContinuePluginService extends PlatformObject implements ViewPart.IDisposable {
    private final java.util.UUID windowId;
    private ContinuePluginWindow continuePluginWindow;
    private IdeProtocolClient ideProtocolClient;
    private CoreMessengerManager coreMessengerManager;
    private String[] workspacePaths;
    private DiffManager diffManager;

    public ContinuePluginService() {
        windowId = Utils.uuid();
    }

    public CoreMessenger getCoreMessenger() {
        return coreMessengerManager != null ? coreMessengerManager.getCoreMessenger() : null;
    }

    @Override
    public void dispose() {
        if (coreMessengerManager != null && coreMessengerManager.getCoreMessenger() != null) {
            coreMessengerManager.getCoreMessenger().killSubProcess();
        }
        // Eclipse에는 코루틴이 없으므로 coroutineScope.cancel()과 같은 부분은 제외했습니다.
    }

    public void sendToWebview(String messageType, Object data, String messageId) {
        if (messageId == null || messageId.isEmpty()) {
            messageId = Utils.uuid().toString();
        }
        if (continuePluginWindow != null && continuePluginWindow.getBrowser() != null) {
            continuePluginWindow.getBrowser().sendToWebview(messageType, data, messageId);
        }
    }

    // Eclipse에서 필요한 메서드들을 추가해야 합니다.
    @Override
    public void init(org.eclipse.ui.IViewSite site) throws org.eclipse.ui.PartInitException {
        super.init(site);
        // 초기화 로직을 여기에 추가하세요.
    }

    @Override
    public void createPartControl(org.eclipse.swt.widgets.Composite parent) {
        // 파트 컨트롤 생성 로직을 여기에 추가하세요.
    }

    @Override
    public void setFocus() {
        // 포커스 설정 로직을 여기에 추가하세요.
    }
}
