package com.github.continuedev.continueeclipseextension.autocomplete;

import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.codeInsight.lookup.LookupEvent;
import com.intellij.codeInsight.lookup.LookupListener;
import com.intellij.codeInsight.lookup.LookupManagerListener;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.Service.Level;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.messages.Topic;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service({Level.PROJECT})
public final class AutocompleteLookupListener implements LookupManagerListener {
   @NotNull
   private final AtomicBoolean isLookupShown;

   public AutocompleteLookupListener(@NotNull Project project) {
      Intrinsics.checkNotNullParameter(project, "project");
      super();
      this.isLookupShown = new AtomicBoolean(true);
      MessageBusConnection var10000 = project.getMessageBus().connect();
      Topic var2 = LookupManagerListener.TOPIC;
      Intrinsics.checkNotNullExpressionValue(var2, "TOPIC");
      var10000.subscribe(var2, this);
   }

   public final boolean isLookupEmpty() {
      return this.isLookupShown.get();
   }

   public void activeLookupChanged(@Nullable Lookup oldLookup, @Nullable Lookup newLookup) {
      if (newLookup != null) {
         final Editor var5 = newLookup.getEditor();
         if (var5 != null) {
            if (newLookup instanceof LookupImpl) {
               newLookup.addLookupListener(new LookupListener() {
                  public void lookupShown(LookupEvent event) {
                     Intrinsics.checkNotNullParameter(event, "event");
                     AutocompleteLookupListener.this.isLookupShown.set(false);
                     ApplicationManager.getApplication().invokeLater(<undefinedtype>::lookupShown$lambda$0);
                  }

                  public void lookupCanceled(LookupEvent event) {
                     Intrinsics.checkNotNullParameter(event, "event");
                     AutocompleteLookupListener.this.isLookupShown.set(true);
                  }

                  public void itemSelected(LookupEvent event) {
                     Intrinsics.checkNotNullParameter(event, "event");
                     AutocompleteLookupListener.this.isLookupShown.set(true);
                  }

                  private static final void lookupShown$lambda$0(LookupEvent $event, Editor $newEditor) {
                     Project var2 = $event.getLookup().getEditor().getProject();
                     if (var2 != null) {
                        ComponentManager $this$service$iv = (ComponentManager)var2;
                        int $i$f$service = 0;
                        Class serviceClass$iv = AutocompleteService.class;
                        Object var7 = $this$service$iv.getService(serviceClass$iv);
                        if (var7 == null) {
                           throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
                        }

                        AutocompleteService var3 = (AutocompleteService)var7;
                        var3.hideCompletions($newEditor);
                     }

                  }
               });
            }

            return;
         }
      }

   }
}
