package com.github.continuedev.continueeclipseextension.auth;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class ContinueAuthDialog extends DialogWrapper {
    private final boolean useOnboarding;
    private final OnTokenEnteredListener onTokenEnteredListener;
    private JBTextField tokenField;

    interface OnTokenEnteredListener {
        void onTokenEntered(String token);
    }

    public ContinueAuthDialog(boolean useOnboarding, OnTokenEnteredListener onTokenEnteredListener) {
        super(true);
        this.useOnboarding = useOnboarding;
        this.onTokenEnteredListener = onTokenEnteredListener;
        this.tokenField = new JBTextField();
        this.init();
        setTitle("Continue authentication");
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String message = useOnboarding ? 
            "후보딩을 완료하면 인증 토큰이 표시됩니다. 여기에 입력해주세요:" : 
            "Continue 인증 토큰을 입력해주세요:";
        panel.add(new JBLabel(message), BorderLayout.NORTH);
        panel.add(tokenField, BorderLayout.CENTER);
        return panel;
    }

    @Override
    protected void doOKAction() {
        String token = tokenField.getText();
        if (!token.trim().isEmpty()) {
            onTokenEnteredListener.onTokenEntered(token);
            super.doOKAction();
        } else {
            setErrorText("유효한 토큰을 입력해주세요");
        }
    }
}