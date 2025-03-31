package com.github.continuedev.continueeclipseextension.autocomplete;

import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.messages.Topic;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class AutocompleteEditorListener implements EditorFactoryListener {
	@NotNull
	private final Map disposables = (Map)(new LinkedHashMap());
	
	public void editorCreated(@NotNull EditorFactoryEvent event) {
	   Intrinsics.checkNotNullParameter(event, "event");
	   Editor project = event.getEditor();
	   Intrinsics.checkNotNullExpressionValue(project, "getEditor(...)");
	   final Editor editor = project;
	   Project completionProvider = project.getProject();
	   if (completionProvider != null) {
	      Project var10 = completionProvider;
	      boolean documentListener = false;
	      FileEditorManager var11 = FileEditorManager.getInstance(completionProvider);
	      if (var11 != null) {
	         FileEditorManager editorManager = var11;
	         ComponentManager $this$service$iv = (ComponentManager)var10;
	         int $i$f$service = 0;
	         Class serviceClass$iv = AutocompleteService.class;
	         Object var9 = $this$service$iv.getService(serviceClass$iv);
	         if (var9 == null) {
	            throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
	         } else {
	            MessageBusConnection var10000;
	            label25: {
	               completionProvider = (AutocompleteService)var9;
	               caretListener = new AutocompleteCaretListener();
	               editor.getCaretModel().addCaretListener(caretListener);
	               Project var16 = editor.getProject();
	               if (var16 != null) {
	                  MessageBus var19 = var16.getMessageBus();
	                  if (var19 != null) {
	                     var10000 = var19.connect();
	                     break label25;
	                  }
	               }
	
	               var10000 = null;
	            }
	
	            final MessageBusConnection connection = var10000;
	            if (connection != null) {
	               Topic var17 = FileEditorManagerListener.FILE_EDITOR_MANAGER;
	               Intrinsics.checkNotNullExpressionValue(var17, "FILE_EDITOR_MANAGER");
	               connection.subscribe(var17, new FileEditorManagerListener() {
	                  public void selectionChanged(FileEditorManagerEvent event) {
	                     Intrinsics.checkNotNullParameter(event, "event");
	                     AutocompleteService.clearCompletions$default(completionProvider, editor, (PendingCompletion)null, 2, (Object)null);
	                  }
	               });
	            }
	
	            final AutocompleteDocumentListener documentListener = new AutocompleteDocumentListener(editorManager, editor);
	            editor.getDocument().addDocumentListener(documentListener);
	            this.disposables.put(editor, new Function0() {
	               public final void invoke() {
	                  editor.getCaretModel().removeCaretListener(caretListener);
	                  MessageBusConnection var1 = connection;
	                  if (var1 != null) {
	                     var1.disconnect();
	                  }
	
	                  editor.getDocument().removeDocumentListener(documentListener);
	               }
	
	               // $FF: synthetic method
	               // $FF: bridge method
	               public Object invoke() {
	                  this.invoke();
	                  return Unit.INSTANCE;
	               }
	            });
	         }
	      }
	   }
	}
	
	public void editorReleased(@NotNull EditorFactoryEvent event) {
	   Intrinsics.checkNotNullParameter(event, "event");
	   Editor disposable = event.getEditor();
	   Intrinsics.checkNotNullExpressionValue(disposable, "getEditor(...)");
	   Editor editor = disposable;
	   Function0 disposable = (Function0)this.disposables.get(disposable);
	   if (disposable != null) {
	      disposable.invoke();
	   }
	
	   this.disposables.remove(editor);
	}
}
