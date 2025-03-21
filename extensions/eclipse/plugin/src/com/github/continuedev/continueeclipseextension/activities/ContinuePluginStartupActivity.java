package com.github.continuedev.continueeclipseextension.activities;

import com.github.continuedev.continueeclipseextension.auth.AuthListener;
import com.github.continuedev.continueeclipseextension.auth.ContinueAuthService;
import com.github.continuedev.continueeclipseextension.auth.ControlPlaneSessionInfo;
import com.github.continuedev.continueeclipseextension.constants.ContinueConstants;
import com.github.continuedev.continueeclipseextension.listener.ContinuePluginSelectionListener;
import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.KeyBinding;
import org.eclipse.jface.action.KeyStroke;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.KeyBindingAction;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContinuePluginStartupActivity implements IStartup {

    @Override
    public void earlyStartup() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.addPerspectiveListener(new IPerspectiveListener() {
            @Override
            public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
                removeKeyBindingFromActions("J", "control");
                removeKeyBindingFromActions("shift J", "control");
                removeKeyBindingFromActions("I", "control");
                initializePlugin(page);

                // Eclipse와 IntelliJ의 불필요한 차이를 제거하거나 대체하는 주석.
                // 이를 실제 Eclipse의 기능에 맞게 코딩해야 합니다.
            }

            @Override
            public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
                // Placeholder for perspective changed
            }
        });
    }

    private void removeKeyBindingFromActions(String key, String modifier) {
        IAction action = AbstractTextEditor.getActionRegistry().getAction("continue." + key.toLowerCase());
        if (action == null || !(action instanceof KeyBindingAction)) return;

        KeyBindingAction keyBindingAction = (KeyBindingAction) action;
        KeyBinding keyBinding = keyBindingAction.getKeyBinding();
        if (keyBinding != null && keyBinding.getKeySequence().equals(getKeySequence(modifier + " " + key))) {
            keyBindingAction.setKeyBinding(null);
        }
        // 명시적인 KeyStroke 제거는 Eclipse IDE에서 제공하는 방법으로 대체해야 합니다.
    }

    private KeySequence getKeySequence(String shortcut) {
        try {
            return KeySequence.getInstance(shortcut);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializePlugin(IWorkbenchPage page) {
        // 여기에 코루틴과 같은 비동기 처리는 있는 대로 Eclipse 방식의 스레딩으로 작성해야 합니다.
        continuePluginService(page.getProject());
        settingsListener(page.getProject());
        // 파일 변경 리스너 같은 부분도 Eclipse의 리소스 변경 감지 시스템을 사용해야 합니다.
        // 같은 개념이지만 구현이 완전히 다릅니다.
    }

    private void continuePluginService(IProject project) {
        ContinuePluginService pluginService = new ContinuePluginService();
        pluginService.init(project);

        ContinueExtensionSettings settings = new ContinueExtensionSettings();
        if (!settings.getState().isShownWelcomeDialog()) {
            settings.getState().setShownWelcomeDialog(true);
            showTutorial(project);
        }

        // 위와 같은 구조로 코루틴 부분을 Eclipse 스레딩으로 재구성해야 합니다.
        // 예를 들어, Display.getDefault().asyncExec(Runnable runnable) 사용 등.
    }

    private void settingsListener(IProject project) {
        // Eclipse 부스체 메시지 버스와 같은 대체 코드를 추가합니다.
        // 이 코드는 Eclipse의 설정 변경 감지를 위한 것입니다.
    }

    private void showTutorial(IProject project) {
        String tutorialFileName = getTutorialFileName();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(tutorialFileName);
        if (is == null) {
            throw new IOException("Resource not found: " + tutorialFileName);
        }

        try {
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            content = content.replace("[Cmd + L]", "[Cmd + J]");
            content = content.replace("[Cmd + Shift + L]", "[Cmd + Shift + J]");

            if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
                content = content.replace("[Cmd + J]", "[Ctrl + J]");
                content = content.replace("[Cmd + Shift + J]", "[Ctrl + Shift + J]");
                content = content.replace("[Cmd + I]", "[Ctrl + I]");
                content = content.replace("⌘", "⌃");
            }

            String filepath = Paths.get(ContinueConstants.getContinueGlobalPath(), tutorialFileName).toString();
            File file = new File(filepath);
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));

            IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            if (workbenchPage != null) {
                IFile resourceFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filepath));
                if (resourceFile.exists()) {
                    Display.getDefault().asyncExec(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                IDE.openEditor(workbenchPage, resourceFile);
                            } catch (PartInitException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTutorialFileName() {
        String appName = "eclipse"; // 시작할 때 이 값을 설정할 때 주의하기 바랍니다. 또는 디스패치 시스템을 통해 동적으로 받아야 합니다.
        return switch (appName.toLowerCase()) {
            case "intellij" -> "continue_tutorial.java";
            case "pycharm" -> "continue_tutorial.py";
            case "webstorm" -> "continue_tutorial.ts";
            default -> "continue_tutorial.py"; // 기본값으로 Python 튜토리얼.
        };
    }

    // 자신이 작성한 ModuleManager, ModuleRootManager, LafManagerListener 등을 대체하거나 구현해야 합니다.
    // 주언의 대부분의 기능은 Eclipse의 제공하는 API나 기능으로 재구성되어야 합니다.
}
