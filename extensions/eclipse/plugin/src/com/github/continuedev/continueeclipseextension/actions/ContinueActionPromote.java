package com.github.continuedev.continueeclipseextension.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.part.EditorPart;

import java.util.List;

public class ContinueActionPromote implements IHandler {

    @Override
    public void addHandlerListener(@NotNull IHandlerListener handlerListener) {
        // 필요에 따라 구현
    }

    @Override
    public void removeHandlerListener(@NotNull IHandlerListener handlerListener) {
        // 필요에 따라 구현
    }

    @Override
    public Object execute(@NotNull ExecutionEvent event) throws ExecutionException {
        // 이 부분은 실제 동작을 위해 구현해야 합니다.
        // 예를 들어, Autocomplete 액션을 실현하거나 체크할 논리가 들어갑니다.
        ITextEditor textEditor = (ITextEditor) EditorPart.getActiveEditor();
        SourceViewer sourceViewer = (SourceViewer) textEditor.getAdapter(SourceViewer.class);
        IContentAssistant contentAssistant = sourceViewer.getContentAssistant();

        if (contentAssistant != null) {
            List<ICompletionProposal> proposals = contentAssistant.computeCompletionProposals(sourceViewer.getDocument(), sourceViewer.getTextWidget().getCaretOffset());
            for (ICompletionProposal proposal : proposals) {
                // AcceptAutocompleteAction에 해당하는 조건을 체크
                if (proposal instanceof AcceptAutocompleteAction) {
                    ContinueExtensionSettings settings = ContinueExtensionSettings.getInstance();
                    if (settings.isContinueStateShowIDECompletionSideBySide()) {
                        // 특정 동작 수행
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean isEnabled() {
        // 필요에 따라 활성화 조건 구현
        return true;
    }

    @Override
    public boolean isHandled() {
        // 필요에 따라 처리 여부 구현
        return true;
    }

    @Override
    public void dispose() {
        // 필요에 따라 자원 해제 구현
    }
}