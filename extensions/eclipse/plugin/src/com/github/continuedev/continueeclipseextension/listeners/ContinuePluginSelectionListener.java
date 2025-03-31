package com.github.continuedev.continueeclipseextension.listeners;

import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.github.continuedev.continueeclipseextension.utils.Debouncer;
import com.intellij.codeWithMe.ClientId;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.event.SelectionEvent;
import com.intellij.openapi.editor.event.SelectionListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import kotlin.ResultKt;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ContinuePluginSelectionListener implements SelectionListener, DumbAware {
   @NotNull
   private final Debouncer debouncer;
   @NotNull
   private ArrayList toolTipComponents;
   @Nullable
   private Editor lastActiveEditor;

   public ContinuePluginSelectionListener(@NotNull CoroutineScope coroutineScope) {
      Intrinsics.checkNotNullParameter(coroutineScope, "coroutineScope");
      super();
      this.debouncer = new Debouncer(100L, coroutineScope);
      this.toolTipComponents = new ArrayList();
   }

   public void selectionChanged(@NotNull final SelectionEvent e) {
      Intrinsics.checkNotNullParameter(e, "e");
      if (!e.getEditor().isDisposed()) {
         Project var2 = e.getEditor().getProject();
         if (!(var2 != null ? var2.isDisposed() : false)) {
            this.debouncer.debounce(new Function1((Continuation)null) {
               int label;

               public final Object invokeSuspend(Object $result) {
                  Object var2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  switch (this.label) {
                     case 0:
                        ResultKt.throwOnFailure($result);
                        ContinuePluginSelectionListener.this.handleSelection(e);
                        return Unit.INSTANCE;
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }
               }

               public final Continuation create(Continuation $completion) {
                  return (Continuation)(new <anonymous constructor>($completion));
               }

               public final Object invoke(Continuation p1) {
                  return ((<undefinedtype>)this.create(p1)).invokeSuspend(Unit.INSTANCE);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object invoke(Object p1) {
                  return this.invoke((Continuation)p1);
               }
            });
            return;
         }
      }

   }

   private final void removeAllTooltips() {
      ApplicationManager.getApplication().invokeLater(ContinuePluginSelectionListener::removeAllTooltips$lambda$1);
   }

   private final void handleSelection(SelectionEvent e) {
      ApplicationManager.getApplication().invokeLater(ContinuePluginSelectionListener::handleSelection$lambda$2);
   }

   private final boolean isFileEditor(Editor editor) {
      Project fileEditorManager = editor.getProject();
      if (fileEditorManager != null) {
         VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
         if (virtualFile != null && virtualFile.isInLocalFileSystem()) {
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(fileEditorManager);
            FileEditor fileEditor = fileEditorManager.getSelectedEditor(virtualFile);
            return fileEditor instanceof TextEditor;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private final boolean shouldRemoveTooltip(String selectedText, Editor editor) {
      CharSequence $i$f$service = (CharSequence)selectedText;
      boolean var10000;
      if ($i$f$service != null && $i$f$service.length() != 0) {
         int $i$f$service = 0;
         Class serviceClass$iv = ContinueExtensionSettings.class;
         Object var5 = ApplicationManager.getApplication().getService(serviceClass$iv);
         if (var5 == null) {
            String var10002 = serviceClass$iv.getName();
            throw new RuntimeException("Cannot find service " + var10002 + " (classloader=" + serviceClass$iv.getClassLoader() + ", client=" + ClientId.Companion.getCurrentOrNull() + ")");
         }

         if (((ContinueExtensionSettings)var5).getContinueState().getDisplayEditorTooltip()) {
            var10000 = false;
            return var10000;
         }
      }

      var10000 = true;
      return var10000;
   }

   private final void removeExistingTooltips(Editor editor, Function0 onComplete) {
      ApplicationManager.getApplication().invokeLater(ContinuePluginSelectionListener::removeExistingTooltips$lambda$4);
   }

   // $FF: synthetic method
   static void removeExistingTooltips$default(ContinuePluginSelectionListener var0, Editor var1, Function0 var2, int var3, Object var4) {
      if ((var3 & 2) != 0) {
         var2 = null.INSTANCE;
      }

      var0.removeExistingTooltips(var1, var2);
   }

   private final void updateTooltip(final Editor editor, final SelectionModel model) {
      this.removeExistingTooltips(editor, new Function0() {
         public final void invoke() {
            ApplicationManager.getApplication().invokeLater(<undefinedtype>::invoke$lambda$0);
         }

         private static final void invoke$lambda$0(Editor $editor, ContinuePluginSelectionListener this$0, SelectionModel $model) {
            Document var4 = $editor.getDocument();
            Intrinsics.checkNotNullExpressionValue(var4, "getDocument(...)");
            Document document = var4;
            Triple var11 = this$0.getSelectionInfo($model, var4);
            int startLine = ((Number)var11.component1()).intValue();
            int endLine = ((Number)var11.component2()).intValue();
            boolean isFullLineSelection = (Boolean)var11.component3();
            boolean isEntireFileSelected = $model.getSelectionStart() == 0 && $model.getSelectionEnd() == document.getTextLength();
            if (isEntireFileSelected) {
               $editor.getScrollingModel().scrollTo(new LogicalPosition(0, 0), ScrollType.CENTER);
            }

            int selectionTopY = this$0.calculateSelectionTopY($editor, startLine, endLine, isFullLineSelection);
            Integer tooltipX = this$0.calculateTooltipX($editor, document, startLine, endLine, isFullLineSelection);
            if (tooltipX != null) {
               this$0.addToolTipComponent($editor, tooltipX, selectionTopY);
            }

         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object invoke() {
            this.invoke();
            return Unit.INSTANCE;
         }
      });
   }

   private final Triple getSelectionInfo(SelectionModel model, Document document) {
      int startOffset = model.getSelectionStart();
      int endOffset = model.getSelectionEnd();
      int startLine = document.getLineNumber(startOffset);
      int endLine = document.getLineNumber(endOffset);
      boolean isFullLineSelection = startOffset == document.getLineStartOffset(startLine) && (endLine > 0 && endOffset == document.getLineEndOffset(endLine - 1) || endOffset == document.getLineStartOffset(endLine));
      int adjustedEndLine = isFullLineSelection && endLine > startLine ? endLine - 1 : endLine;
      return new Triple(startLine, adjustedEndLine, isFullLineSelection);
   }

   private final int calculateSelectionTopY(Editor editor, int startLine, int endLine, boolean isFullLineSelection) {
      int var10000;
      if (startLine != endLine && !isFullLineSelection) {
         var10000 = editor.logicalPositionToXY(new LogicalPosition(startLine, 0)).y;
      } else {
         int lineTopY = editor.logicalPositionToXY(new LogicalPosition(startLine, 0)).y;
         var10000 = lineTopY + editor.getLineHeight() / 2;
      }

      return var10000;
   }

   private final Integer calculateTooltipX(Editor editor, Document document, int startLine, int endLine, boolean isFullLineSelection) {
      int offset = 40;
      if (startLine == endLine && calculateTooltipX$isLineEmpty(document, startLine) && !isFullLineSelection) {
         return null;
      } else {
         int topNonEmptyLine;
         for(topNonEmptyLine = startLine; topNonEmptyLine <= endLine && calculateTooltipX$isLineEmpty(document, topNonEmptyLine); ++topNonEmptyLine) {
         }

         if (topNonEmptyLine > endLine) {
            return null;
         } else if (!isFullLineSelection && startLine != endLine) {
            int lineAboveSelection = Math.max(0, startLine - 1);
            int xCoordTopNonEmpty = calculateTooltipX$getLineEndX(document, editor, topNonEmptyLine);
            int xCoordLineAbove = calculateTooltipX$getLineEndX(document, editor, lineAboveSelection);
            int baseXCoord = Math.max(xCoordTopNonEmpty, xCoordLineAbove);
            return baseXCoord + offset;
         } else {
            return calculateTooltipX$getLineEndX(document, editor, topNonEmptyLine) + offset;
         }
      }
   }

   private final void addToolTipComponent(Editor editor, int tooltipX, int selectionTopY) {
      ToolTipComponent toolTipComponent = new ToolTipComponent(editor, tooltipX, selectionTopY);
      this.toolTipComponents.add(toolTipComponent);
      editor.getContentComponent().add((Component)toolTipComponent);
      editor.getContentComponent().revalidate();
      editor.getContentComponent().repaint();
   }

   private static final void removeAllTooltips$lambda$1(ContinuePluginSelectionListener this$0) {
      Iterable $this$forEach$iv = (Iterable)this$0.toolTipComponents;
      int $i$f$forEach = 0;

      for(Object element$iv : $this$forEach$iv) {
         ToolTipComponent tooltip = (ToolTipComponent)element$iv;
         int var6 = 0;
         Container var7 = tooltip.getParent();
         if (var7 != null) {
            var7.remove((Component)tooltip);
         }
      }

      this$0.toolTipComponents.clear();
   }

   private static final void handleSelection$lambda$2(SelectionEvent $e, ContinuePluginSelectionListener this$0) {
      Editor editor = $e.getEditor();
      Intrinsics.checkNotNull(editor);
      if (!this$0.isFileEditor(editor)) {
         this$0.removeAllTooltips();
      } else {
         if (!Intrinsics.areEqual(editor, this$0.lastActiveEditor)) {
            this$0.removeAllTooltips();
            this$0.lastActiveEditor = editor;
         }

         SelectionModel selectedText = editor.getSelectionModel();
         Intrinsics.checkNotNullExpressionValue(selectedText, "getSelectionModel(...)");
         SelectionModel model = selectedText;
         String selectedText = selectedText.getSelectedText();
         if (this$0.shouldRemoveTooltip(selectedText, editor)) {
            removeExistingTooltips$default(this$0, editor, (Function0)null, 2, (Object)null);
         } else {
            this$0.updateTooltip(editor, model);
         }
      }
   }

   private static final void removeExistingTooltips$lambda$4(ContinuePluginSelectionListener this$0, Editor $editor, Function0 $onComplete) {
      Iterable $this$forEach$iv = (Iterable)this$0.toolTipComponents;
      int $i$f$forEach = 0;

      for(Object element$iv : $this$forEach$iv) {
         ToolTipComponent it = (ToolTipComponent)element$iv;
         int var8 = 0;
         $editor.getContentComponent().remove((Component)it);
      }

      $editor.getContentComponent().revalidate();
      $editor.getContentComponent().repaint();
      this$0.toolTipComponents.clear();
      $onComplete.invoke();
   }

   private static final boolean calculateTooltipX$isLineEmpty(Document $document, int lineNumber) {
      int lineStartOffset = $document.getLineStartOffset(lineNumber);
      int lineEndOffset = $document.getLineEndOffset(lineNumber);
      String var4 = $document.getText(new TextRange(lineStartOffset, lineEndOffset));
      Intrinsics.checkNotNullExpressionValue(var4, "getText(...)");
      CharSequence var5 = (CharSequence)StringsKt.trim((CharSequence)var4).toString();
      return var5.length() == 0;
   }

   private static final int calculateTooltipX$getLineEndX(Document $document, Editor $editor, int lineNumber) {
      int lineStartOffset = $document.getLineStartOffset(lineNumber);
      int lineEndOffset = $document.getLineEndOffset(lineNumber);
      String visualPosition = $document.getText(new TextRange(lineStartOffset, lineEndOffset));
      Intrinsics.checkNotNullExpressionValue(visualPosition, "getText(...)");
      String lineText = StringsKt.trimEnd((CharSequence)visualPosition).toString();
      VisualPosition visualPosition = $editor.offsetToVisualPosition(lineStartOffset + lineText.length());
      Intrinsics.checkNotNullExpressionValue(visualPosition, "offsetToVisualPosition(...)");
      return $editor.visualPositionToXY(visualPosition).x;
   }
}
