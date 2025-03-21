package com.github.continuedev.continueeclipseextension.actions;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;

import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;

import org.eclipse.swt.widgets.Shell;

import java.util.HashMap;
import java.util.Map;

public class ContinuePluginActions {

    public static ContinuePluginService getPluginService(org.eclipse.core.resources.IProject project) {
        if (project == null) {
            return null;
        }
        // Eclipse에서는 직접 비즈니스 로직 서비스를 가져오는 방법이 없으므로
        // 아래와 같이 플러그인의 활성화된 서비스를 가져오는 등의 방법이 필요할 수 있습니다.
        // 여기서는 간단히 예시입니다.
        return ContinuePluginService.getInstance(project);
    }

    public static ContinuePluginService getContinuePluginService(org.eclipse.core.resources.IProject project) {
        if (project != null) {
            final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            // Eclipse에서는 ToolWindow와 유사한 개념으로 Part를 이용합니다.
            // 아래는 Continue Tool Part를 활성화하는 예시입니다.
            // 필요한 경우 FindPart 등의 방법을 사용하여 Part를 찾습니다.
            org.eclipse.ui.IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.github.continuedev.continueeclipseextension.views.ContinueView");
            if (part != null) {
                // Part가 보이지 않는 경우 활성화합니다.
                if (part.isDirty()) {
                    // ToolPart에 해당하는 활성화 로직이 필요합니다.
                }
            }
        }
        return getPluginService(project);
    }

    public static void focusContinueInput(org.eclipse.core.resources.IProject project) {
        ContinuePluginService continuePluginService = getContinuePluginService(project);
        if (continuePluginService != null) {
            continuePluginService.focusContinueInputWithoutClear();
            continuePluginService.sendToWebview("focusContinueInputWithoutClear", null);

            continuePluginService.getIdeProtocolClient().sendHighlightedCode();
        }
    }
}

class AcceptDiffAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        acceptHorizontalDiff();
        acceptVerticalDiff();
    }

    private void acceptHorizontalDiff() {
        org.eclipse.core.resources.IProject project = window.getActivePage().getActiveEditor().getEditorInput().getAdapter(org.eclipse.core.resources.IProject.class);
        ContinuePluginService continuePluginService = ContinuePluginActions.getPluginService(project);
        if (continuePluginService != null) {
            continuePluginService.getDiffManager().acceptDiff(null);
        }
    }

    private void acceptVerticalDiff() {
        org.eclipse.core.resources.IProject project = window.getActivePage().getActiveEditor().getEditorInput().getAdapter(org.eclipse.core.resources.IProject.class);
        IEditorPart editorPart = window.getActivePage().getActiveEditor();
        if (editorPart instanceof ITextEditor) {
            ITextEditor textEditor = (ITextEditor) editorPart;
            ISourceViewer sourceViewer = (ISourceViewer) textEditor.getAdapter(ITextViewer.class);
            if (sourceViewer != null) {
                DiffStreamService diffStreamService = new DiffStreamService(project);
                diffStreamService.accept(sourceViewer.getTextWidget());
            }
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void selectionChanged(IAction action, org.eclipse.jface.viewers.ISelection selection) {
    }
}

class RejectDiffAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        rejectHorizontalDiff();
        rejectVerticalDiff();
    }

    private void rejectHorizontalDiff() {
        org.eclipse.core.resources.IProject project = window.getActivePage().getActiveEditor().getEditorInput().getAdapter(org.eclipse.core.resources.IProject.class);
        ContinuePluginService continuePluginService = ContinuePluginActions.getPluginService(project);
        if (continuePluginService != null) {
            continuePluginService.getDiffManager().rejectDiff(null);
        }
    }

    private void rejectVerticalDiff() {
        org.eclipse.core.resources.IProject project = window.getActivePage().getActiveEditor().getEditorInput().getAdapter(org.eclipse.core.resources.IProject.class);
        IEditorPart editorPart = window.getActivePage().getActiveEditor();
        if (editorPart instanceof ITextEditor) {
            ITextEditor textEditor = (ITextEditor) editorPart;
            ISourceViewer sourceViewer = (ISourceViewer) textEditor.getAdapter(ITextViewer.class);
            if (sourceViewer != null) {
                DiffStreamService diffStreamService = new DiffStreamService(project);
                diffStreamService.reject(sourceViewer.getTextWidget());
            }
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void selectionChanged(IAction action, org.eclipse.jface.viewers.ISelection selection) {
    }
}

class FocusContinueInputWithoutClearAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        org.eclipse.core.resources.IProject project = window.getActivePage().getActiveEditor().getEditorInput().getAdapter(org.eclipse.core.resources.IProject.class);
        ContinuePluginActions.focusContinueInput(project);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void selectionChanged(IAction action, org.eclipse.jface.viewers.ISelection selection) {
    }
}

class FocusContinueInputAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        org.eclipse.core.resources.IProject project = window.getActivePage().getActiveEditor().getEditorInput().getAdapter(org.eclipse.core.resources.IProject.class);
        ContinuePluginService continuePluginService = ContinuePluginActions.getContinuePluginService(project);
        if (continuePluginService != null) {
            continuePluginService.focusContinueInputWithoutClear();
            continuePluginService.sendToWebview("focusContinueInputWithNewSession", null);

            continuePluginService.getIdeProtocolClient().sendHighlightedCode();
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void selectionChanged(IAction action, org.eclipse.jface.viewers.ISelection selection) {
    }
}

class NewContinueSessionAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        org.eclipse.core.resources.IProject project = window.getActivePage().getActiveEditor().getEditorInput().getAdapter(org.eclipse.core.resources.IProject.class);
        ContinuePluginService continuePluginService = ContinuePluginActions.getContinuePluginService(project);
        if (continuePluginService != null) {
            continuePluginService.focusContinueInputWithoutClear();
            continuePluginService.sendToWebview("focusContinueInputWithNewSession", null);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void selectionChanged(IAction action, org.eclipse.jface.viewers.ISelection selection) {
    }
}

class ViewHistoryAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        org.eclipse.core.resources.IProject project = window.getActivePage().getActiveEditor().getEditorInput().getAdapter(org.eclipse.core.resources.IProject.class);
        ContinuePluginService continuePluginService = ContinuePluginActions.getContinuePluginService(project);
        if (continuePluginService != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("path", "/history");
            params.put("toggle", true);
            continuePluginService.sendToWebview("navigateTo", params);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void selectionChanged(IAction action, org.eclipse.jface.viewers.ISelection selection) {
    }
}

class OpenConfigAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        org.eclipse.core.resources.IProject project = window.getActivePage().getActiveEditor().getEditorInput().getAdapter(org.eclipse.core.resources.IProject.class);
        ContinuePluginService continuePluginService = ContinuePluginActions.getContinuePluginService(project);
        if (continuePluginService != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("path", "/config");
            params.put("toggle", true);
            continuePluginService.sendToWebview("navigateTo", params);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void selectionChanged(IAction action, org.eclipse.jface.viewers.ISelection selection) {
    }
}

class OpenMorePageAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        org.eclipse.core.resources.IProject project = window.getActivePage().getActiveEditor().getEditorInput().getAdapter(org.eclipse.core.resources.IProject.class);
        ContinuePluginService continuePluginService = ContinuePluginActions.getContinuePluginService(project);
        if (continuePluginService != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("path", "/more");
            params.put("toggle", true);
            continuePluginService.sendToWebview("navigateTo", params);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void selectionChanged(IAction action, org.eclipse.jface.viewers.ISelection selection) {
    }
}