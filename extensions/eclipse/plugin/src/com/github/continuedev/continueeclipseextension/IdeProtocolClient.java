package com.github.continuedev.continueeclipseextension;

import com.github.continuedev.continueeclipseextension.*;
import com.github.continuedev.continueeclipseextension.auth.AuthListener;
import com.github.continuedev.continueeclipseextension.auth.ContinueAuthService;
import com.github.continuedev.continueeclipseextension.editor.DiffStreamHandler;
import com.github.continuedev.continueeclipseextension.editor.DiffStreamService;
import com.github.continuedev.continueeclipseextension.protocol.*;
import com.github.continuedev.continueeclipseextension.services.*;
import com.github.continuedev.continueeclipseextension.utils.*;
import com.google.gson.Gson;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IdeProtocolClient implements DumbAware {
    private final ContinuePluginService continuePluginService;
    private final ServiceScope coroutineScope;
    private final Project project;

    private final IDE ide;

    public IdeProtocolClient(ContinuePluginService continuePluginService, ServiceScope coroutineScope, Project project) {
        this.continuePluginService = continuePluginService;
        this.coroutineScope = coroutineScope;
        this.project = project;

        this.ide = new EclipseIde(project, continuePluginService);

        VirtualFileManager.getInstance().addAsyncFileListener(
                new AsyncFileSaveListener(continuePluginService), ContinuePluginDisposable.getInstance(project)
        );
    }

    public void handleMessage(String msg, ResponseCallback respond) {
        coroutineScope.launch(Dispatchers.IO, () -> {
            Gson gson = new Gson();
            Message message = gson.fromJson(msg, Message.class);
            String messageType = message.getMessageType();
            Object dataElement = message.getData();

            try {
                switch (messageType) {
                    case "toggleDevTools":
                        continuePluginService.getContinuePluginWindow().getBrowser().openDevTools();
                        break;

                    case "showTutorial":
                        showTutorial(project);
                        break;

                    case "jetbrains/isOSREnabled":
                        boolean isOSREnabled = ServiceManager.getService(ContinueExtensionSettings.class).getContinueState().isEnableOSR();
                        respond.onResponse(isOSREnabled);
                        break;

                    case "jetbrains/getColors":
                        Map<String, String> colors = GetTheme.getTheme();
                        respond.onResponse(colors);
                        break;

                    case "jetbrains/onLoad":
                        Map<String, Object> jsonData = new HashMap<>();
                        jsonData.put("windowId", continuePluginService.getWindowId());
                        jsonData.put("workspacePaths", continuePluginService.getWorkspacePaths());
                        jsonData.put("vscMachineId", getMachineUniqueID());
                        jsonData.put("vscMediaUrl", "http://continue");
                        respond.onResponse(jsonData);
                        break;

                    case "getIdeSettings":
                        Map<String, Object> settings = ide.getIdeSettings();
                        respond.onResponse(settings);
                        break;

                    case "getControlPlaneSessionInfo":
                        GetControlPlaneSessionInfoParams params = gson.fromJson(dataElement.toString(), GetControlPlaneSessionInfoParams.class);
                        ContinueAuthService authService = service(ContinueAuthService.class);

                        if (params.isSilent()) {
                            Map<String, Object> sessionInfo = authService.loadControlPlaneSessionInfo();
                            respond.onResponse(sessionInfo);
                        } else {
                            authService.startAuthFlow(project, params.useOnboarding());
                            respond.onResponse(null);
                        }
                        break;

                    case "logoutOfControlPlane":
                        authService = service(ContinueAuthService.class);
                        authService.signOut();
                        ApplicationManager.getApplication().getMessageBus().syncPublisher(AuthListener.TOPIC)
                                .handleUpdatedSessionInfo(null);

                        // Tell the webview that session info changed
                        continuePluginService.sendToWebview("didChangeControlPlaneSessionInfo", null, UUID.randomUUID().toString());

                        respond.onResponse(null);
                        break;

                    case "getIdeInfo":
                        Map<String, Object> ideInfo = ide.getIdeInfo();
                        respond.onResponse(ideInfo);
                        break;

                    case "getUniqueId":
                        String uniqueId = ide.getUniqueId();
                        respond.onResponse(uniqueId);
                        break;

                    case "copyText":
                        CopyTextParams copyParams = gson.fromJson(dataElement.toString(), CopyTextParams.class);
                        String textToCopy = copyParams.getText();
                        Clipboard clipboard = new Clipboard(Display.getDefault());
                        clipboard.setContents(new Object[]{textToCopy}, new Transfer[]{TextTransfer.getInstance()});
                        clipboard.dispose();
                        respond.onResponse(null);
                        break;

                    case "showDiff":
                        ShowDiffParams showDiffParams = gson.fromJson(dataElement.toString(), ShowDiffParams.class);
                        ide.showDiff(showDiffParams.getFilepath(), showDiffParams.getNewContents(), showDiffParams.getStepIndex());
                        respond.onResponse(null);
                        break;

                    case "readFile":
                        ReadFileParams readFileParams = gson.fromJson(dataElement.toString(), ReadFileParams.class);
                        String contents = ide.readFile(readFileParams.getFilepath());
                        respond.onResponse(contents);
                        break;

                    case "isTelemetryEnabled":
                        boolean isEnabled = ide.isTelemetryEnabled();
                        respond.onResponse(isEnabled);
                        break;

                    case "readRangeInFile":
                        ReadRangeInFileParams readRangeParams = gson.fromJson(dataElement.toString(), ReadRangeInFileParams.class);
                        String rangeContents = ide.readRangeInFile(readRangeParams.getFilepath(), readRangeParams.getRange());
                        respond.onResponse(rangeContents);
                        break;

                    case "getWorkspaceDirs":
                        List<String> dirs = ide.getWorkspaceDirs();
                        respond.onResponse(dirs);
                        break;

                    case "getTags":
                        GetTagsParams getTagsParams = gson.fromJson(dataElement.toString(), GetTagsParams.class);
                        List<String> tags = ide.getTags(getTagsParams.getArtifactId());
                        respond.onResponse(tags);
                        break;

                    case "getWorkspaceConfigs":
                        Map<String, Object> workspaceConfigs = ide.getWorkspaceConfigs();
                        respond.onResponse(workspaceConfigs);
                        break;

                    case "getTerminalContents":
                        String terminalContents = ide.getTerminalContents();
                        respond.onResponse(terminalContents);
                        break;

                    case "saveFile":
                        SaveFileParams saveFileParams = gson.fromJson(dataElement.toString(), SaveFileParams.class);
                        ide.saveFile(saveFileParams.getFilepath());
                        respond.onResponse(null);
                        break;

                    case "showVirtualFile":
                        ShowVirtualFileParams showVirtualFileParams = gson.fromJson(dataElement.toString(), ShowVirtualFileParams.class);
                        ide.showVirtualFile(showVirtualFileParams.getName(), showVirtualFileParams.getContent());
                        respond.onResponse(null);
                        break;

                    case "showLines":
                        ShowLinesParams showLinesParams = gson.fromJson(dataElement.toString(), ShowLinesParams.class);
                        ide.showLines(showLinesParams.getFilepath(), showLinesParams.getStartLine(), showLinesParams.getEndLine());
                        respond.onResponse(null);
                        break;

                    case "getFileStats":
                        GetFileStatsParams getFileStatsParams = gson.fromJson(dataElement.toString(), GetFileStatsParams.class);
                        Map<String, Object> fileStatsMap = ide.getFileStats(getFileStatsParams.getFiles());
                        respond.onResponse(fileStatsMap);
                        break;

                    case "listDir":
                        ListDirParams listDirParams = gson.fromJson(dataElement.toString(), ListDirParams.class);
                        List<String> files = ide.listDir(listDirParams.getDir());
                        respond.onResponse(files);
                        break;

                    case "getGitRootPath":
                        GetGitRootPathParams gitRootPathParams = gson.fromJson(dataElement.toString(), GetGitRootPathParams.class);
                        String rootPath = ide.getGitRootPath(gitRootPathParams.getDir());
                        respond.onResponse(rootPath);
                        break;

                    case "getBranch":
                        GetBranchParams branchParams = gson.fromJson(dataElement.toString(), GetBranchParams.class);
                        String branch = ide.getBranch(branchParams.getDir());
                        respond.onResponse(branch);
                        break;

                    case "getRepoName":
                        GetRepoNameParams repoNameParams = gson.fromJson(dataElement.toString(), GetRepoNameParams.class);
                        String repoName = ide.getRepoName(repoNameParams.getDir());
                        respond.onResponse(repoName);
                        break;

                    case "getDiff":
                        GetDiffParams diffParams = gson.fromJson(dataElement.toString(), GetDiffParams.class);
                        List<String> diffs = ide.getDiff(diffParams.isIncludeUnstaged());
                        respond.onResponse(diffs);
                        break;

                    case "getProblems":
                        List<String> problems = ide.getProblems();
                        respond.onResponse(problems);
                        break;

                    case "writeFile":
                        WriteFileParams writeFileParams = gson.fromJson(dataElement.toString(), WriteFileParams.class);
                        ide.writeFile(writeFileParams.getPath(), writeFileParams.getContents());
                        respond.onResponse(null);
                        break;

                    case "fileExists":
                        FileExistsParams fileExistsParams = gson.fromJson(dataElement.toString(), FileExistsParams.class);
                        boolean exists = ide.fileExists(fileExistsParams.getFilepath());
                        respond.onResponse(exists);
                        break;

                    case "openFile":
                        OpenFileParams openFileParams = gson.fromJson(dataElement.toString(), OpenFileParams.class);
                        ide.openFile(openFileParams.getPath());
                        respond.onResponse(null);
                        break;

                    case "runCommand":
                        // Running commands not yet supported in Eclipse
                        respond.onResponse(null);
                        break;

                    case "showToast":
                        List<Object> jsonArray = (List<Object>) dataElement;

                        // Get toast type from first element, default to INFO if invalid
                        String typeStr = jsonArray.size() > 0 ? (String) jsonArray.get(0) : ToastType.INFO.getValue();
                        ToastType type = ToastType.fromValue(typeStr);

                        // Get message from second element
                        String toastMessage = jsonArray.size() > 1 ? (String) jsonArray.get(1) : "";

                        // Get remaining elements as otherParams
                        String[] otherParams = jsonArray.size() > 2 ? jsonArray.subList(2, jsonArray.size()).toArray(new String[0]) : new String[0];

                        Object result = ide.showToast(type, toastMessage, otherParams);
                        respond.onResponse(result);
                        break;

                    case "getSearchResults":
                        GetSearchResultsParams searchResultsParams = gson.fromJson(dataElement.toString(), GetSearchResultsParams.class);
                        List<SearchResult> results = ide.getSearchResults(searchResultsParams.getQuery());
                        respond.onResponse(results);
                        break;

                    case "getOpenFiles":
                        List<String> openFiles = ide.getOpenFiles();
                        respond.onResponse(openFiles);
                        break;

                    case "getCurrentFile":
                        String currentFile = ide.getCurrentFile();
                        respond.onResponse(currentFile);
                        break;

                    case "getPinnedFiles":
                        List<String> pinnedFiles = ide.getPinnedFiles();
                        respond.onResponse(pinnedFiles);
                        break;

                    case "getGitHubAuthToken":
                        GetGhTokenArgs ghTokenArgs = gson.fromJson(dataElement.toString(), GetGhTokenArgs.class);
                        String ghAuthToken = ide.getGitHubAuthToken(ghTokenArgs);

                        if (ghAuthToken == null) {
                            // Open a dialog so user can enter their GitHub token
                            continuePluginService.sendToWebview("openOnboardingCard", null, UUID.randomUUID().toString());
                            respond.onResponse(null);
                        } else {
                            respond.onResponse(ghAuthToken);
                        }
                        break;

                    case "setGitHubAuthToken":
                        SetGitHubAuthTokenParams authTokenParams = gson.fromJson(dataElement.toString(), SetGitHubAuthTokenParams.class);
                        ContinueExtensionSettings continueSettingsService = service(ContinueExtensionSettings.class);
                        continueSettingsService.getContinueState().setGhAuthToken(authTokenParams.getToken());
                        respond.onResponse(null);
                        break;

                    case "openUrl":
                        OpenUrlParam urlParam = gson.fromJson(dataElement.toString(), OpenUrlParam.class);
                        ide.openUrl(urlParam.getUrl());
                        respond.onResponse(null);
                        break;

                    case "insertAtCursor":
                        InsertAtCursorParams insertParams = gson.fromJson(dataElement.toString(), InsertAtCursorParams.class);
                        Display.getDefault().asyncExec(() -> {
                            ITextEditor editor = getSelectedTextEditor();
                            if (editor == null) return;
                            ITextSelection selection = (ITextSelection) editor.getSelectionProvider().getSelection();
                            IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());

                            try {
                                document.replace(selection.getOffset(), selection.getLength(), insertParams.getText());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        respond.onResponse(null);
                        break;

                    case "applyToFile":
                        ApplyToFileParams applyParams = gson.fromJson(dataElement.toString(), ApplyToFileParams.class);
                        ITextEditor editor = getSelectedTextEditor();

                        if (editor == null) {
                            ide.showToast(ToastType.ERROR, "No active editor to apply edits to");
                            respond.onResponse(null);
                            return;
                        }

                        IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
                        if (document.get().trim().isEmpty()) {
                            try {
                                document.replace(0, 0, applyParams.getText());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            respond.onResponse(null);
                            return;
                        }

                        Any llm;
                        try {
                            ResponseFuture responseFuture = continuePluginService.getCoreMessenger().request(
                                    "config/getSerializedProfileInfo", null, null
                            );

                            Map<String, Object> responseObject = (Map<String, Object>) responseFuture.getResponse();
                            Map<String, Object> responseContent = (Map<String, Object>) responseObject.get("content");
                            Map<String, Object> result = (Map<String, Object>) responseContent.get("result");
                            Map<String, Object> config = (Map<String, Object>) result.get("config");

                            Map<String, Object> selectedModels = (Map<String, Object>) config.get("selectedModelByRole");
                            llm = selectedModels != null ? selectedModels.get("apply") : null;

                            if (llm == null) {
                                List<Map<String, Object>> models = (List<Map<String, Object>>) config.get("models");
                                llm = models.stream()
                                        .filter(model -> model.get("title").equals(applyParams.getCurSelectedModelTitle()))
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalStateException("Model '" + applyParams.getCurSelectedModelTitle() + "' not found in config."));
                            }
                        } catch (Exception e) {
                            PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
                                ide.showToast(ToastType.ERROR, "Failed to fetch model configuration");
                            });
                            respond.onResponse(null);
                            return;
                        }

                        DiffStreamService diffStreamService = project.service(DiffStreamService.class);
                        diffStreamService.reject(editor);

                        String llmTitle = (String) ((Map<String, Object>) llm).get("title");

                        String prompt = "The following code was suggested as an edit:\n```\n" + applyParams.getText() + "\n```\nPlease apply it to the previous code.";

                        RangeInFileWithContents rif = getHighlightedCode();

                        String prefix, highlighted, suffix;
                        if (rif == null) {
                            // If no highlight, use the whole document as highlighted
                            prefix = "";
                            highlighted = document.get();
                            suffix = "";
                        } else {
                            prefix = document.get(rif.getRange().getStart().getCharacter());
                            highlighted = rif.getContents();
                            suffix = document.get(rif.getRange().getEnd().getCharacter(), document.getLength());

                            // Remove the selection after processing
                            PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
                                editor.getSelectionProvider().setSelection(new TextSelection(
                                        editor.getDocumentProvider().getDocument(editor.getEditorInput()),
                                        rif.getRange().getStart().getLine(),
                                        rif.getRange().getEnd().getLine()
                                ));
                            });

                            Document doc = document;
                            prefix = doc.get().substring(0, doc.getLineOffset(rif.getRange().getStart().getLine()));
                            highlighted = doc.get().substring(doc.getLineOffset(rif.getRange().getStart().getLine()), doc.getLineOffset(rif.getRange().getEnd().getLine()));
                            suffix = doc.get().substring(doc.getLineOffset(rif.getRange().getEnd().getLine()));
                        }

                        DiffStreamHandler diffStreamHandler = new DiffStreamHandler(project, editor, rif != null ? rif.getRange().getStart().getLine() : 0,
                                rif != null ? rif.getRange().getEnd().getLine() : document.getNumberOfLines() - 1, () -> {}, () -> {});

                        diffStreamService.register(diffStreamHandler, editor);

                        diffStreamHandler.streamDiffLinesToEditor(prompt, prefix, highlighted, suffix, llmTitle);

                        respond.onResponse(null);
                        break;

                    default:
                        System.out.println("Unknown message type: " + messageType);
                        break;
                }
            } catch (Exception error) {
                ide.showToast(ToastType.ERROR, "Error handling message of type " + messageType + ": " + error.getMessage());
            }
        });
    }

    private RangeInFileWithContents getHighlightedCode() {
        RangeInFileWithContents rif = null;
        ApplicationManager.getApplication().runReadAction(() -> {
            // Get the editor instance for the currently active editor window
            ITextEditor editor = getSelectedTextEditor();
            IFile virtualFile = editor != null ? (IFile) editor.getEditorInput().getAdapter(IFile.class) : null;

            // Get the selection range and content
            ITextSelection selectionModel = (ITextSelection) editor.getSelectionProvider().getSelection();
            String selectedText = selectionModel.getText();

            IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
            int startOffset = selectionModel.getOffset();
            int endOffset = selectionModel.getOffset() + selectionModel.getLength();

            if (startOffset == endOffset) {
                rif = null;
                return;
            }

            int startLine = document.getLineOfOffset(startOffset) + 1;
            int endLine = document.getLineOfOffset(endOffset) + 1;

            int startChar = startOffset - document.getLineOffset(startLine - 1);
            int endChar = endOffset - document.getLineOffset(endLine - 1);

            if (virtualFile != null) {
                rif = new RangeInFileWithContents(virtualFile.getFullPath().toString(), new Range(
                        new Position(startLine, startChar),
                        new Position(endLine, endChar)
                ), selectedText);
            }
        });
        return rif;
    }

    public void sendHighlightedCode(boolean edit) {
        RangeInFileWithContents rif = getHighlightedCode();
        if (rif == null) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("rangeInFileWithContents", rif);
        data.put("edit", edit);
        continuePluginService.sendToWebview("highlightedCode", data);
    }

    public void sendHighlightedCode() {
        sendHighlightedCode(false);
    }

    public void sendAcceptRejectDiff(boolean accepted, int stepIndex) {
        continuePluginService.sendToWebview("acceptRejectDiff", new AcceptRejectDiff(accepted, stepIndex), uuid());
    }

    public void deleteAtIndex(int index) {
        continuePluginService.sendToWebview("deleteAtIndex", new DeleteAtIndex(index), uuid());
    }
}