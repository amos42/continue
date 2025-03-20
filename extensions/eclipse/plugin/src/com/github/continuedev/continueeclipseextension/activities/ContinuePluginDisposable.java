package com.github.continuedev.continueeclipseextension.activities;

import com.github.continuedev.continueeclipseextension.auth.AuthListener;
import com.github.continuedev.continueeclipseextension.auth.ContinueAuthService;
import com.github.continuedev.continueeclipseextension.auth.ControlPlaneSessionInfo;
import com.github.continuedev.continueeclipseextension.constants.Constants;
import com.github.continuedev.continueeclipseextension.continuemodule.ContinueExtensionSettings;
import com.github.continuedev.continueeclipseextension.continuemodule.ContinuePluginService;
import com.github.continuedev.continueeclipseextension.listeners.ContinuePluginSelectionListener;
import com.github.continuedev.continueeclipseextension.listeners.SettingsListener;
import com.github.continuedev.continueeclipseextension.utils.UriUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStartup;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.part.FileEditorInput;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContinuePluginStartupActivity implements IStartup, Constants {

    private ExecutorService executorService;

    @Override
    public void earlyStartup() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> initializePlugin());
    }

    private void showTutorial(IProject project) {
        String tutorialFileName = getTutorialFileName();

        InputStream is = this.getClass().getClassLoader().getResourceAsStream(tutorialFileName);
        if (is == null) {
            throw new IOException("Resource not found: " + tutorialFileName);
        }
        String content;
        try {
            content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // All jetbrains will use J instead of L
        content = content.replace("[Cmd + L]", "[Cmd + J]");
        content = content.replace("[Cmd + Shift + L]", "[Cmd + Shift + J]");

        if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
            content = content.replace("[Cmd + J]", "[Ctrl + J]");
            content = content.replace("[Cmd + Shift + J]", "[Ctrl + Shift + J]");
            content = content.replace("[Cmd + I]", "[Ctrl + I]");
            content = content.replace("⌘", "⌃");
        }
        String filepath = Paths.get(getContinueGlobalPath(), tutorialFileName).toString();
        try {
            Files.write(Paths.get(filepath), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        IFile virtualFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new org.eclipse.core.runtime.Path(filepath));
        Display.getDefault().asyncExec(() -> {
            if (virtualFile != null) {
                try {
                    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    if (window != null) {
                        IWorkbenchPage page = window.getActivePage();
                        if (page != null) {
                            page.openEditor(new FileEditorInput(virtualFile), "org.eclipse.ui.genericeditor.GenericTextEditor");
                        }
                    }
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getTutorialFileName() {
        String appName = "Eclipse"; // Eclipse에서는 fullProductName 개념이 없으므로, Eclipse로 고정
        appName = appName.toLowerCase();
        if (appName.contains("eclipse")) {
            return "continue_tutorial.java"; // Eclipse는 Java Tutorial 사용
        } else {
            return "continue_tutorial.py"; // Default to Python tutorial
        }
    }

    private void initializePlugin() {
        removeShortcutFromAction(getPlatformSpecificKeyStroke("J"));
        removeShortcutFromAction(getPlatformSpecificKeyStroke("shift J"));
        removeShortcutFromAction(getPlatformSpecificKeyStroke("I"));

        ContinuePluginService continuePluginService = ContinuePluginService.getInstance();

        // Settings 처리
        continuePluginService.executeAsync(() -> {
            ContinueExtensionSettings settings = ContinueExtensionSettings.getInstance();
            if (!settings.isShownWelcomeDialog()) {
                settings.setShownWelcomeDialog(true);
                showTutorial(null); // Eclipse에서는 프로젝트 개념이 다르므로, null로 전달
            }

            try {
                settings.addRemoteSyncJob();
            } catch (CoreException e) {
                e.printStackTrace();
            }

            // TODO: 여기에서 필요한 이벤트 리스너들을 구현하는 등의 작업이 필요할 것입니다.
        });
    }

    private String getPlatformSpecificKeyStroke(String key) {
        String osName = System.getProperty("os.name").toLowerCase();
        String modifier = osName.contains("mac") ? "meta" : "control";
        return modifier + " " + key;
    }

    private void removeShortcutFromAction(String shortcut) {
        IBindingService bindingService = PlatformUI.getWorkbench().getAdapter(IBindingService.class);
        try {
            KeyStroke keyStroke = KeyStroke.getInstance(shortcut);
            if (bindingService != null) {
                String[] activeBindingIds = bindingService.getActiveBindingsFor(keyStroke);
                for (String bindingId : activeBindingIds) {
                    if (!bindingId.startsWith("continue")) {
                        continue;
                    }
                    bindingService.triggerSequence(keyStroke, new EventMappingService.ActiveBindingConfiguration[] {}, SWT.NONE);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
