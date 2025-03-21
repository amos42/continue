package com.github.continuedev.continueeclipseextension;

import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DiffManager {
    public static class DiffInfo {
        String originalFilepath;
        String newFilepath;
        Dialog dialog;
        int stepIndex;
    }

    private final Map<String, DiffInfo> diffInfoMap;
    private String lastFile2;
    private final Shell shell;

    public DiffManager() {
        this.diffInfoMap = new HashMap<>();
        this.shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    public static File getDiffDirectory() {
        String homeDirectory = System.getProperty("user.home");
        Path diffDirPath = Paths.get(homeDirectory).resolve(".continue").resolve(".diffs");
        File diffDir = diffDirPath.toFile();
        if (!diffDir.exists()) {
            diffDir.mkdirs();
            diffDir.setWritable(true);
        }
        return diffDir;
    }

    public static String escapeFilepath(String filepath) {
        return filepath.replace("/", "_f_").replace("\\", "_b_").replace(":", "_c_");
    }

    public void showDiff(String filepath, String replacement, int stepIndex) {
        File diffDir = getDiffDirectory();
        String escapedPath = escapeFilepath(filepath);
        File file = new File(diffDir, escapedPath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.write(file.toPath(), replacement.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        openDiffWindow(filepath, file.getAbsolutePath(), stepIndex);
    }

    private void cleanUpFile(String file2) {
        DiffInfo diffInfo = diffInfoMap.get(file2);
        if (diffInfo != null && diffInfo.dialog != null) {
            diffInfo.dialog.close();
        }
        diffInfoMap.remove(file2);
        new File(file2).delete();
        if (lastFile2 != null && lastFile2.equals(file2)) {
            lastFile2 = null;
        }
    }

    public void acceptDiff(String file2) {
        String file = file2 != null ? file2 : lastFile2;
        if (file == null) return;
        DiffInfo diffInfo = diffInfoMap.get(file);
        if (diffInfo == null) return;

        try {
            ContinuePluginService continuePluginService = (ContinuePluginService) PlatformUI.getWorkbench()
                    .getService(ContinuePluginService.class);
            if (continuePluginService != null && continuePluginService.getIdeProtocolClient() != null) {
                continuePluginService.getIdeProtocolClient().sendAcceptRejectDiff(true, diffInfo.stepIndex);
            }
            Files.write(new File(diffInfo.originalFilepath).toPath(), Files.readAllBytes(new File(file).toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cleanUpFile(file);
    }

    public void rejectDiff(String file2) {
        String file = file2 != null ? file2 : lastFile2;
        if (file == null) return;
        DiffInfo diffInfo = diffInfoMap.get(file);
        if (diffInfo == null) return;

        ContinuePluginService continuePluginService = (ContinuePluginService) PlatformUI.getWorkbench()
                .getService(ContinuePluginService.class);
        if (continuePluginService != null && continuePluginService.getIdeProtocolClient() != null) {
            continuePluginService.getIdeProtocolClient().deleteAtIndex(diffInfo.stepIndex);
            continuePluginService.getIdeProtocolClient().sendAcceptRejectDiff(false, diffInfo.stepIndex);
        }

        cleanUpFile(file);
    }

    private void openDiffWindow(String file1, String file2, int stepIndex) {
        lastFile2 = file2;

        try {
            String content1 = new String(Files.readAllBytes(new File(file1).toPath()));
            String content2 = new String(Files.readAllBytes(new File(file2).toPath()));

            DiffInfo diffInfo = diffInfoMap.get(file2);
            boolean shouldShowDialog = diffInfo == null;
            if (shouldShowDialog) {
                diffInfo = new DiffInfo();
                diffInfo.originalFilepath = file1;
                diffInfo.newFilepath = file2;
                diffInfo.stepIndex = stepIndex;
                diffInfoMap.put(file2, diffInfo);
            }

            Shell dialogShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            dialogShell.setLayout(new FillLayout());
            dialogShell.setText("Continue Diff");

            Text text1 = new Text(dialogShell, SWT.V_SCROLL | SWT.BORDER | SWT.READ_ONLY);
            text1.setText(content1);

            Text text2 = new Text(dialogShell, SWT.V_SCROLL | SWT.BORDER | SWT.READ_ONLY);
            text2.setText(content2);

            Button acceptButton = new Button(dialogShell, SWT.PUSH);
            acceptButton.setText("Accept (ALT + SHIFT + Y)");
            acceptButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    acceptDiff(file2);
                    dialogShell.close();
                }
            });

            Button rejectButton = new Button(dialogShell, SWT.PUSH);
            rejectButton.setText("Reject (ALT + SHIFT + N)");
            rejectButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    rejectDiff(file2);
                    dialogShell.close();
                }
            });

            text1.setSize(400, 300);
            text2.setSize(400, 300);

            Rectangle screenSize = shell.getDisplay().getPrimaryMonitor().getBounds();
            dialogShell.setSize(screenSize.width / 2, screenSize.height / 2);
            dialogShell.setLocation((screenSize.width - dialogShell.getSize().x) / 2,
                    (screenSize.height - dialogShell.getSize().y) / 2);

            if (shouldShowDialog) {
                diffInfo.dialog = new Dialog(dialogShell) {
                    @Override
                    protected Control createDialogArea(Composite parent) {
                        Composite container = (Composite) super.createDialogArea(parent);
                        container.setLayout(new FillLayout());

                        text1.moveAbove(text2);

                        return container;
                    }

                    @Override
                    protected void createButtonsForButtonBar(Composite parent) {
                        Button okButton = createButton(parent, IDialogConstants.OK_ID, "Accept", true);
                        okButton.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                acceptDiff(file2);
                                close();
                            }
                        });

                        Button cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, "Reject", false);
                        cancelButton.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                rejectDiff(file2);
                                close();
                            }
                        });
                    }
                };
            }

            dialogShell.open();
            while (!dialogShell.isDisposed()) {
                if (!dialogShell.getDisplay().readAndDispatch()) {
                    dialogShell.getDisplay().sleep();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}