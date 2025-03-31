package com.github.continuedev.continueeclipseextension.auth;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JPanel;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

public final class ContinueAuthDialog extends DialogWrapper {
   private final boolean useOnboarding;
   @NotNull
   private final Function1 onTokenEntered;
   @NotNull
   private final JBTextField tokenField;

   public ContinueAuthDialog(boolean useOnboarding, @NotNull Function1 onTokenEntered) {
      Intrinsics.checkNotNullParameter(onTokenEntered, "onTokenEntered");
      super(true);
      this.useOnboarding = useOnboarding;
      this.onTokenEntered = onTokenEntered;
      this.tokenField = new JBTextField();
      this.init();
      this.setTitle("Continue authentication");
   }

   @NotNull
   protected JComponent createCenterPanel() {
      JPanel panel = new JPanel((LayoutManager)(new BorderLayout()));
      String message = this.useOnboarding ? "After onboarding you will be shown an authentication token. Please enter it here:" : "Please enter your Continue authentication token:";
      panel.add((Component)(new JBLabel(message)), "North");
      panel.add((Component)this.tokenField, "Center");
      return (JComponent)panel;
   }

   protected void doOKAction() {
      String token = this.tokenField.getText();
      Intrinsics.checkNotNull(token);
      if (!StringsKt.isBlank((CharSequence)token)) {
         this.onTokenEntered.invoke(token);
         super.doOKAction();
      } else {
         this.setErrorText("Please enter a valid token");
      }

   }
}
