package com.github.continuedev.continueeclipseextension.autocomplete;

import com.github.continuedev.continueeclipseextension.CoreMessenger;
import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.github.continuedev.continueeclipseextension.utils.UtilsKt;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.Service.Level;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.InlayProperties;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Computable;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;


@Service({Level.PROJECT})
public final class AutocompleteService {
	public final class shouldRenderCompletion implements Computable {
	   // $FF: synthetic field
	   final int $offset$inlined;
	   // $FF: synthetic field
	   final Editor $editor$inlined;

	   public shouldRenderCompletion(int var1, Editor var2) {
	      this.$offset$inlined = var1;
	      this.$editor$inlined = var2;
	   }

	   public final Object compute() {
	      int var1 = 0;
	      return this.$offset$inlined != this.$editor$inlined.getCaretModel().getOffset();
	   }
	}

	public final class accept implements Runnable {
	   // $FF: synthetic field
	   final AutocompleteService this$0;
	   // $FF: synthetic field
	   final Editor $editor$inlined;
	   // $FF: synthetic field
	   final PendingCompletion $completion$inlined;

	   public accept(AutocompleteService var1, Editor var2, PendingCompletion var3) {
	      this.this$0 = var1;
	      this.$editor$inlined = var2;
	      this.$completion$inlined = var3;
	   }

	   public final void run() {
	      int var1 = 0;
	      this.this$0.clearCompletions(this.$editor$inlined, this.$completion$inlined);
	   }
	}
	
	public final class isInjectedFile implements Computable {
	   // $FF: synthetic field
	   final AutocompleteService this$0;
	   // $FF: synthetic field
	   final Editor $editor$inlined;

	   public isInjectedFile(AutocompleteService var1, Editor var2) {
	      this.this$0 = var1;
	      this.$editor$inlined = var2;
	   }

	   public final Object compute() {
	      int var1 = 0;
	      PsiFile var2 = PsiDocumentManager.getInstance(AutocompleteService.access$getProject$p(this.this$0)).getPsiFile(this.$editor$inlined.getDocument());
	      boolean var10000;
	      if (var2 != null) {
	         boolean var3 = AutocompleteServiceKt.isInjectedText((PsiElement)var2);
	         var10000 = var3;
	      } else {
	         var10000 = false;
	      }

	      return var10000;
	   }
	}
	
	@NotNull
	private final Project project;
	@Nullable
	private PendingCompletion pendingCompletion;
	@NotNull
	private final AutocompleteLookupListener autocompleteLookupListener;
	@NotNull
	private final Lazy widget$delegate;
	private boolean lastChangeWasPartialAccept;
	
	public AutocompleteService(@NotNull Project project) {
	   Intrinsics.checkNotNullParameter(project, "project");
	   super();
	   this.project = project;
	   ComponentManager $this$service$iv = (ComponentManager)this.project;
	   int $i$f$service = 0;
	   Class serviceClass$iv = AutocompleteLookupListener.class;
	   Object var5 = $this$service$iv.getService(serviceClass$iv);
	   if (var5 == null) {
	      throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
	   } else {
	      this.autocompleteLookupListener = (AutocompleteLookupListener)var5;
	      this.widget$delegate = LazyKt.lazy(new Function0() {
	         public final AutocompleteSpinnerWidget invoke() {
	            StatusBar var2 = WindowManager.getInstance().getStatusBar(AutocompleteService.this.project);
	            StatusBarWidget var1 = var2 != null ? var2.getWidget("AutocompleteSpinnerWidget") : null;
	            return var1 instanceof AutocompleteSpinnerWidget ? (AutocompleteSpinnerWidget)var1 : null;
	         }
	
	         // $FF: synthetic method
	         // $FF: bridge method
	         public Object invoke() {
	            return this.invoke();
	         }
	      });
	   }
	}
	
	@Nullable
	public final PendingCompletion getPendingCompletion() {
	   return this.pendingCompletion;
	}
	
	public final void setPendingCompletion(@Nullable PendingCompletion var1) {
	   this.pendingCompletion = var1;
	}
	
	private final AutocompleteSpinnerWidget getWidget() {
	   Lazy var1 = this.widget$delegate;
	   Object var2 = null;
	   return (AutocompleteSpinnerWidget)var1.getValue();
	}
	
	public final boolean getLastChangeWasPartialAccept() {
	   return this.lastChangeWasPartialAccept;
	}
	
	public final void setLastChangeWasPartialAccept(boolean var1) {
	   this.lastChangeWasPartialAccept = var1;
	}
	
	public final void triggerCompletion(@NotNull final Editor editor) {
	   Intrinsics.checkNotNullParameter(editor, "editor");
	   ContinueExtensionSettings settings = (ContinueExtensionSettings)ServiceManager.getService(ContinueExtensionSettings.class);
	   if (settings.getContinueState().getEnableTabAutocomplete()) {
	      if (this.pendingCompletion != null) {
	         PendingCompletion var10001 = this.pendingCompletion;
	         Intrinsics.checkNotNull(var10001);
	         clearCompletions$default(this, var10001.getEditor(), (PendingCompletion)null, 2, (Object)null);
	      }
	
	      final String completionId = UtilsKt.uuid();
	      final int offset = editor.getCaretModel().getPrimaryCaret().getOffset();
	      this.pendingCompletion = new PendingCompletion(editor, offset, completionId, (String)null);
	      VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
	      if (virtualFile != null) {
	         String line = UtilsKt.toUriOrNull(virtualFile);
	         if (line != null) {
	            String uri = line;
	            AutocompleteSpinnerWidget var15 = this.getWidget();
	            if (var15 != null) {
	               var15.setLoading(true);
	            }
	
	            final int line = editor.getCaretModel().getPrimaryCaret().getLogicalPosition().line;
	            int column = editor.getCaretModel().getPrimaryCaret().getLogicalPosition().column;
	            Pair[] var10 = new Pair[]{TuplesKt.to("completionId", completionId), TuplesKt.to("filepath", uri), null, null, null, null};
	            Pair[] $this$service$iv = new Pair[]{TuplesKt.to("line", line), TuplesKt.to("character", column)};
	            var10[2] = TuplesKt.to("pos", MapsKt.mapOf($this$service$iv));
	            var10[3] = TuplesKt.to("clipboardText", "");
	            var10[4] = TuplesKt.to("recentlyEditedRanges", CollectionsKt.emptyList());
	            var10[5] = TuplesKt.to("recentlyVisitedRanges", CollectionsKt.emptyList());
	            Map input = MapsKt.mapOf(var10);
	            ComponentManager $this$service$iv = (ComponentManager)this.project;
	            int $i$f$service = 0;
	            Class serviceClass$iv = ContinuePluginService.class;
	            Object var14 = $this$service$iv.getService(serviceClass$iv);
	            if (var14 == null) {
	               throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
	            }
	
	            CoreMessenger var17 = ((ContinuePluginService)var14).getCoreMessenger();
	            if (var17 != null) {
	               var17.request("autocomplete/complete", input, (String)null, new Function1() {
	                  public final void invoke(Object response) {
	                     label29: {
	                        if (AutocompleteService.this.getPendingCompletion() != null) {
	                           PendingCompletion responseObject = AutocompleteService.this.getPendingCompletion();
	                           if (!Intrinsics.areEqual(responseObject != null ? responseObject.getCompletionId() : null, completionId)) {
	                              break label29;
	                           }
	                        }
	
	                        AutocompleteSpinnerWidget var6 = AutocompleteService.this.getWidget();
	                        if (var6 != null) {
	                           var6.setLoading(false);
	                        }
	                     }
	
	                     Intrinsics.checkNotNull(response, "null cannot be cast to non-null type kotlin.collections.Map<*, *>");
	                     Map responseObject = (Map)response;
	                     Object completion = responseObject.get("content");
	                     Intrinsics.checkNotNull(completion, "null cannot be cast to non-null type kotlin.collections.List<*>");
	                     List completions = (List)completion;
	                     if (!((Collection)completions).isEmpty()) {
	                        String completion = String.valueOf(completions.get(0));
	                        String finalTextToInsert = AutocompleteService.this.deduplicateCompletion(editor, offset, completion);
	                        if (AutocompleteService.this.shouldRenderCompletion(finalTextToInsert, offset, line, editor)) {
	                           AutocompleteService.this.renderCompletion(editor, offset, finalTextToInsert);
	                           AutocompleteService.this.setPendingCompletion(new PendingCompletion(editor, offset, completionId, finalTextToInsert));
	                        }
	                     }
	
	                  }
	
	                  // $FF: synthetic method
	                  // $FF: bridge method
	                  public Object invoke(Object p1) {
	                     this.invoke(p1);
	                     return Unit.INSTANCE;
	                  }
	               });
	            }
	
	            return;
	         }
	      }
	
	   }
	}
	
	private final boolean shouldRenderCompletion(String completion, int offset, int line, Editor editor) {
	   if (((CharSequence)completion).length() != 0) {
	      int $i$f$runReadAction = 0;
	      if (!(Boolean)ApplicationManager.getApplication().runReadAction(new AutocompleteService$shouldRenderCompletion$$inlined$runReadAction$1(offset, editor))) {
	         if (StringsKt.lines((CharSequence)completion).size() == 1) {
	            return true;
	         }
	
	         $i$f$runReadAction = editor.getDocument().getLineEndOffset(line);
	         boolean var10000;
	         if (offset <= $i$f$runReadAction) {
	            String var6 = editor.getDocument().getText(new TextRange(offset, $i$f$runReadAction));
	            Intrinsics.checkNotNullExpressionValue(var6, "getText(...)");
	            if (StringsKt.isBlank((CharSequence)var6)) {
	               var10000 = true;
	               return var10000;
	            }
	         }
	
	         var10000 = false;
	         return var10000;
	      }
	   }
	
	   return false;
	}
	
	private final String deduplicateCompletion(Editor editor, int offset, String completion) {
	   Object var4 = ApplicationManager.getApplication().runReadAction(AutocompleteService::deduplicateCompletion$lambda$2);
	   Intrinsics.checkNotNullExpressionValue(var4, "runReadAction(...)");
	   return (String)var4;
	}
	
	private final void renderCompletion(Editor editor, int offset, String completion) {
	   if (((CharSequence)completion).length() != 0) {
	      if (!this.isInjectedFile(editor)) {
	         Object var4 = ServiceManager.getService(ContinueExtensionSettings.class);
	         Intrinsics.checkNotNullExpressionValue(var4, "getService(...)");
	         if (!this.shouldSkipRender((ContinueExtensionSettings)var4)) {
	            ApplicationManager.getApplication().invokeLater(AutocompleteService::renderCompletion$lambda$4);
	         }
	      }
	   }
	}
	
	public final void accept() {
	   PendingCompletion text = this.pendingCompletion;
	   if (text != null) {
	      PendingCompletion completion = text;
	      String editor = text.getText();
	      if (editor != null) {
	         String var10 = editor;
	         Editor var11 = completion.getEditor();
	         int offset = completion.getOffset();
	         var11.getDocument().insertString(offset, (CharSequence)var10);
	         var11.getCaretModel().moveToOffset(offset + var10.length());
	         ComponentManager $i$f$invokeLater = (ComponentManager)this.project;
	         boolean $i$f$service = false;
	         Class serviceClass$iv = ContinuePluginService.class;
	         Object var9 = $i$f$invokeLater.getService(serviceClass$iv);
	         if (var9 == null) {
	            throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $i$f$invokeLater + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
	         } else {
	            CoreMessenger modalityState$iv = ((ContinuePluginService)var9).getCoreMessenger();
	            if (modalityState$iv != null) {
	               Pair[] var13 = new Pair[]{TuplesKt.to("completionId", completion.getCompletionId())};
	               modalityState$iv.request("autocomplete/accept", MapsKt.hashMapOf(var13), (String)null, null.INSTANCE);
	            }
	
	            ModalityState modalityState$iv = null;
	            int $i$f$invokeLater = 0;
	            Application var10000 = ApplicationManager.getApplication();
	            Runnable var10001 = new AutocompleteService$accept$$inlined$invokeLater$default$1(this, var11, completion);
	            ModalityState var10002 = ModalityState.defaultModalityState();
	            Intrinsics.checkNotNullExpressionValue(var10002, "defaultModalityState()");
	            var10000.invokeLater(var10001, var10002);
	         }
	      }
	   }
	}
	
	private final boolean shouldSkipRender(ContinueExtensionSettings settings) {
	   return !settings.getContinueState().getShowIDECompletionSideBySide() && !this.autocompleteLookupListener.isLookupEmpty();
	}
	
	private final List splitKeepingDelimiters(String input, String delimiterPattern) {
	   CharSequence result = (CharSequence)input;
	   Regex currentDelimiter = new Regex("(?<=" + delimiterPattern + ")|(?=" + delimiterPattern + ')');
	   byte $this$filterTo$iv$iv = 0;
	   Iterable var13 = (Iterable)currentDelimiter.split(result, $this$filterTo$iv$iv);
	   int $i$f$filter = 0;
	   Collection destination$iv$iv = (Collection)(new ArrayList());
	   int $i$f$filterTo = 0;
	
	   for(Object element$iv$iv : var13) {
	      String it = (String)element$iv$iv;
	      int var12 = 0;
	      if (((CharSequence)it).length() > 0) {
	         destination$iv$iv.add(element$iv$iv);
	      }
	   }
	
	   List initialSplit = (List)destination$iv$iv;
	   List result = (List)(new ArrayList());
	   String currentDelimiter = "";
	
	   for(String part : initialSplit) {
	      CharSequence var19 = (CharSequence)part;
	      Regex var20 = new Regex(delimiterPattern);
	      if (var20.matches(var19)) {
	         currentDelimiter = currentDelimiter + part;
	      } else {
	         if (((CharSequence)currentDelimiter).length() > 0) {
	            result.add(currentDelimiter);
	            currentDelimiter = "";
	         }
	
	         result.add(part);
	      }
	   }
	
	   if (((CharSequence)currentDelimiter).length() > 0) {
	      result.add(currentDelimiter);
	   }
	
	   return result;
	}
	
	// $FF: synthetic method
	static List splitKeepingDelimiters$default(AutocompleteService var0, String var1, String var2, int var3, Object var4) {
	   if ((var3 & 2) != 0) {
	      var2 = "\\s+";
	   }
	
	   return var0.splitKeepingDelimiters(var1, var2);
	}
	
	public final void partialAccept() {
	   PendingCompletion text = this.pendingCompletion;
	   if (text != null) {
	      PendingCompletion completion = text;
	      String editor = text.getText();
	      if (editor != null) {
	         String var7 = editor;
	         Editor var8 = completion.getEditor();
	         int offset = completion.getOffset();
	         this.lastChangeWasPartialAccept = true;
	         List words = splitKeepingDelimiters$default(this, var7, (String)null, 2, (Object)null);
	         System.out.println(words);
	         String word = (String)words.get(0);
	         var8.getDocument().insertString(offset, (CharSequence)word);
	         var8.getCaretModel().moveToOffset(offset + word.length());
	         this.hideCompletions(var8);
	         String var10001 = var7.substring(word.length());
	         Intrinsics.checkNotNullExpressionValue(var10001, "this as java.lang.String).substring(startIndex)");
	         completion.setText(var10001);
	         completion.setOffset(completion.getOffset() + word.length());
	         int var10002 = completion.getOffset();
	         String var10003 = completion.getText();
	         Intrinsics.checkNotNull(var10003);
	         this.renderCompletion(var8, var10002, var10003);
	      }
	   }
	}
	
	private final void cancelCompletion(PendingCompletion completion) {
	   AutocompleteSpinnerWidget var2 = this.getWidget();
	   if (var2 != null) {
	      var2.setLoading(false);
	   }
	
	   ComponentManager $this$service$iv = (ComponentManager)this.project;
	   int $i$f$service = 0;
	   Class serviceClass$iv = ContinuePluginService.class;
	   Object var6 = $this$service$iv.getService(serviceClass$iv);
	   if (var6 == null) {
	      throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
	   } else {
	      CoreMessenger var7 = ((ContinuePluginService)var6).getCoreMessenger();
	      if (var7 != null) {
	         var7.request("autocomplete/cancel", (Object)null, (String)null, null.INSTANCE);
	      }
	
	   }
	}
	
	public final void clearCompletions(@NotNull Editor editor, @Nullable PendingCompletion completion) {
	   Intrinsics.checkNotNullParameter(editor, "editor");
	   if (!this.isInjectedFile(editor)) {
	      if (completion != null) {
	         this.cancelCompletion(completion);
	         String var10000 = completion.getCompletionId();
	         PendingCompletion var3 = this.pendingCompletion;
	         if (Intrinsics.areEqual(var10000, var3 != null ? var3.getCompletionId() : null)) {
	            this.pendingCompletion = null;
	         }
	      }
	
	      this.disposeInlayRenderer(editor);
	   }
	}
	
	// $FF: synthetic method
	public static void clearCompletions$default(AutocompleteService var0, Editor var1, PendingCompletion var2, int var3, Object var4) {
	   if ((var3 & 2) != 0) {
	      var2 = var0.pendingCompletion;
	   }
	
	   var0.clearCompletions(var1, var2);
	}
	
	private final boolean isInjectedFile(Editor editor) {
	   int $i$f$runReadAction = 0;
	   return (Boolean)ApplicationManager.getApplication().runReadAction(new AutocompleteService$isInjectedFile$$inlined$runReadAction$1(this, editor));
	}
	
	public final void hideCompletions(@NotNull Editor editor) {
	   Intrinsics.checkNotNullParameter(editor, "editor");
	   if (!this.isInjectedFile(editor)) {
	      this.disposeInlayRenderer(editor);
	   }
	}
	
	private final void disposeInlayRenderer(Editor editor) {
	   List $this$forEach$iv = editor.getInlayModel().getInlineElementsInRange(0, editor.getDocument().getTextLength());
	   Intrinsics.checkNotNullExpressionValue($this$forEach$iv, "getInlineElementsInRange(...)");
	   Iterable $this$forEach$iv = (Iterable)$this$forEach$iv;
	   int $i$f$forEach = 0;
	
	   for(Object element$iv : $this$forEach$iv) {
	      Inlay it = (Inlay)element$iv;
	      int var7 = 0;
	      if (it.getRenderer() instanceof ContinueInlayRenderer) {
	         it.dispose();
	      }
	   }
	
	   List var9 = editor.getInlayModel().getBlockElementsInRange(0, editor.getDocument().getTextLength());
	   Intrinsics.checkNotNullExpressionValue(var9, "getBlockElementsInRange(...)");
	   Iterable $this$forEach$iv = (Iterable)var9;
	   $i$f$forEach = 0;
	
	   for(Object element$iv : $this$forEach$iv) {
	      Inlay it = (Inlay)element$iv;
	      int var15 = 0;
	      if (it.getRenderer() instanceof ContinueInlayRenderer) {
	         it.dispose();
	      }
	   }
	
	}
	
	private static final String deduplicateCompletion$lambda$2(Editor $editor, String $completion) {
	   Document caretOffset = $editor.getDocument();
	   Intrinsics.checkNotNullExpressionValue(caretOffset, "getDocument(...)");
	   Document document = caretOffset;
	   int caretOffset = $editor.getCaretModel().getOffset();
	   if (caretOffset == document.getTextLength()) {
	      return $completion;
	   } else {
	      int N = 10;
	      String newlineIndex = caretOffset + N <= document.getTextLength() ? document.getText(new TextRange(caretOffset, caretOffset + N)) : document.getText(new TextRange(caretOffset, document.getTextLength()));
	      Intrinsics.checkNotNull(newlineIndex);
	      String textAfterCursor = newlineIndex;
	      if (StringsKt.isBlank((CharSequence)newlineIndex)) {
	         return $completion;
	      } else {
	         Integer var8 = StringsKt.indexOf$default((CharSequence)newlineIndex, "\r\n", 0, false, 6, (Object)null);
	         int it = ((Number)var8).intValue();
	         int var10 = 0;
	         Integer indexOfTextAfterCursorInCompletion = it >= 0 ? var8 : null;
	         int newlineIndex = indexOfTextAfterCursorInCompletion != null ? indexOfTextAfterCursorInCompletion : StringsKt.indexOf$default((CharSequence)newlineIndex, '\n', 0, false, 6, (Object)null);
	         if (newlineIndex > 0) {
	            String var10000 = textAfterCursor.substring(0, newlineIndex);
	            Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String…ing(startIndex, endIndex)");
	            textAfterCursor = var10000;
	         }
	
	         int indexOfTextAfterCursorInCompletion = StringsKt.indexOf$default((CharSequence)$completion, textAfterCursor, 0, false, 6, (Object)null);
	         if (indexOfTextAfterCursorInCompletion > 0) {
	            return StringsKt.slice($completion, new IntRange(0, indexOfTextAfterCursorInCompletion - 1));
	         } else {
	            return indexOfTextAfterCursorInCompletion == 0 ? "" : $completion;
	         }
	      }
	   }
	}
	
	private static final void renderCompletion$lambda$4$lambda$3(AutocompleteService this$0, Editor $editor, String $completion, int $offset) {
	   this$0.hideCompletions($editor);
	   InlayProperties properties = new InlayProperties();
	   properties.relatesToPrecedingText(true);
	   properties.disableSoftWrapping(true);
	   List lines = StringsKt.lines((CharSequence)$completion);
	   PendingCompletion var6 = this$0.pendingCompletion;
	   this$0.pendingCompletion = var6 != null ? PendingCompletion.copy$default(var6, (Editor)null, 0, (String)null, CollectionsKt.joinToString$default((Iterable)lines, (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null), 7, (Object)null) : null;
	   AutocompleteServiceKt.addInlayElement($editor, lines, $offset, properties);
	}
	
	private static final void renderCompletion$lambda$4(AutocompleteService this$0, Editor $editor, String $completion, int $offset) {
	   WriteAction.run(AutocompleteService::renderCompletion$lambda$4$lambda$3);
	}
}
