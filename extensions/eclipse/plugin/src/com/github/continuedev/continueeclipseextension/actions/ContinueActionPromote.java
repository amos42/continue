package com.github.continuedev.continueeclipseextension.actions;

import com.github.continuedev.continueeclipseextension.autocomplete.AcceptAutocompleteAction;
import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.intellij.openapi.actionSystem.ActionPromoter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.components.ServiceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ContinueActionPromote implements ActionPromoter {
   @Nullable
   public List promote(@NotNull List actions, @NotNull DataContext context) {
      Intrinsics.checkNotNullParameter(actions, "actions");
      Intrinsics.checkNotNullParameter(context, "context");
      Iterable $this$none$iv = (Iterable)actions;
      int $i$f$none = 0;
      boolean var10000;
      if ($this$none$iv instanceof Collection && ((Collection)$this$none$iv).isEmpty()) {
         var10000 = true;
      } else {
         Iterator $i$f$filterIsInstance = $this$none$iv.iterator();

         while(true) {
            if (!$i$f$filterIsInstance.hasNext()) {
               var10000 = true;
               break;
            }

            Object element$iv = $i$f$filterIsInstance.next();
            AnAction it = (AnAction)element$iv;
            int $i$f$filterIsInstanceTo = 0;
            if (it instanceof AcceptAutocompleteAction) {
               var10000 = false;
               break;
            }
         }
      }

      if (var10000) {
         return null;
      } else {
         ContinueExtensionSettings settings = (ContinueExtensionSettings)ServiceManager.getService(ContinueExtensionSettings.class);
         if (!settings.getContinueState().getShowIDECompletionSideBySide()) {
            return null;
         } else {
            Iterable $this$filterIsInstance$iv = (Iterable)actions;
            int $i$f$filterIsInstance = 0;
            Collection destination$iv$iv = (Collection)(new ArrayList());
            int $i$f$filterIsInstanceTo = 0;

            for(Object element$iv$iv : $this$filterIsInstance$iv) {
               if (element$iv$iv instanceof AcceptAutocompleteAction) {
                  destination$iv$iv.add(element$iv$iv);
               }
            }

            return (List)destination$iv$iv;
         }
      }
   }
}
