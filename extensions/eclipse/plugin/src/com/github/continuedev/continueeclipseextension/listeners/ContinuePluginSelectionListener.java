package com.github.continuedev.continueeclipseextension.listeners;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.SelectionTracker;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ContinuePluginSelectionListener implements ISelectionChangedListener {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final List<ToolTipComponent> toolTipComponents = new ArrayList<>();
    private ITextEditor lastActiveEditor = null;

    public ContinuePluginSelectionListener() {
        ISelectionProvider provider = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelectionProvider();
        if (provider instanceof SelectionTracker) {
            ((SelectionTracker) provider).addSelectionChangedListener(this);
        }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (!(editorPart instanceof ITextEditor) || editorPart.isDisposed()) {
            return;
        }

        ITextEditor editor = (ITextEditor) editorPart;
        IDocumentProvider provider = editor.getDocumentProvider();
        ISelection selection = event.getSelection();

        if (selection instanceof ITextSelection) {
            ITextSelection textSelection = (ITextSelection) selection;

            executorService.schedule(() -> handleSelection(editor, textSelection), 100, TimeUnit.MILLISECONDS);
        }
    }

    private void removeAllTooltips() {
        PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
            toolTipComponents.forEach(tooltip -> {
                Composite parent = tooltip.getParent();
                if (parent != null) {
                    parent.removeControl(tooltip);
                }
            });
            toolTipComponents.clear();
        });
    }

    private void handleSelection(ITextEditor editor, ITextSelection selection) {
        if (editor != lastActiveEditor) {
            removeAllTooltips();
            lastActiveEditor = editor;
        }

        if (shouldRemoveTooltip(selection.getText(), editor)) {
            removeExistingTooltips(editor);
            return;
        }

        updateTooltip(editor, selection);
    }

    private boolean shouldRemoveTooltip(String selectedText, ITextEditor editor) {
        ContinueExtensionSettings settings = ContinueExtensionSettings.getInstance();
        if (settings == null) {
            return true; // Handle cases where settings instance is not available
        }
        return selectedText == null || selectedText.isEmpty() || !settings.continueState.displayEditorTooltip;
    }

    private void removeExistingTooltips(ITextEditor editor) {
        PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
            Composite contentComponent = editor.getAdapter(Composite.class);
            if (contentComponent != null) {
                toolTipComponents.forEach(tooltip -> contentComponent.removeControl(tooltip));
                contentComponent.layout();
                contentComponent.redraw();
            }
            toolTipComponents.clear();
        });
    }

    private void updateTooltip(ITextEditor editor, ITextSelection selection) {
        removeExistingTooltips(editor);
        PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
            if (editor.isDisposed()) {
                return;
            }

            Composite contentComponent = editor.getAdapter(Composite.class);
            if (contentComponent != null) {
                Point selectionTopY = calculateSelectionTopY(editor, selection);
                Point tooltipX = calculateTooltipX(editor, selection);

                if (tooltipX != null) {
                    ToolTipComponent toolTipComponent = new ToolTipComponent(editor, tooltipX.x, selectionTopY.y);
                    toolTipComponents.add(toolTipComponent);
                    contentComponent.add(tooltipX.x, selectionTopY.y, toolTipComponent);
                    contentComponent.layout();
                    contentComponent.redraw();
                }
            }
        });
    }

    private Point calculateSelectionTopY(ITextEditor editor, ITextSelection selection) {
        // In Eclipse, it's necessary to adapt to the editor's widget or use other means to get exact positions
        return editor.getAdapter(Composite.class).toDisplay(selection.getOffset(), selection.getLine());
    }

    private Point calculateTooltipX(ITextEditor editor, ITextSelection selection) {
        final int offset = 40;
        // Add logic to calculate X coordinate based on selected lines
        return new Point(selection.getOffset() + offset, 0); // Simplified example
    }

    // Assuming ToolTipComponent has a constructor which accepts an Editor, X position, and Y position.
    private void addToolTipComponent(ITextEditor editor, Point tooltipX, Point selectionTopY) {
        ToolTipComponent toolTipComponent = new ToolTipComponent(editor, tooltipX.x, selectionTopY.y);
        toolTipComponents.add(toolTipComponent);
        Composite contentComponent = editor.getAdapter(Composite.class);
        if (contentComponent != null) {
            contentComponent.addControl(toolTipComponent);
            contentComponent.layout();
            contentComponent.redraw();
        }
    }
}