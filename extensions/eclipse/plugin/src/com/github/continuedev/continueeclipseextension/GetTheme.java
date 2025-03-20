package com.github.continuedev.continueeclipseextension;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class GetTheme {
    private static final int TINT = 20;

    public RGB getSecondaryDark() {
        ITheme theme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
        RGB defaultBackground = theme.getColorRegistry().getRGBValue("org.eclipse.ui.workbench.background");

        int grayscale = (int) (defaultBackground.red * 0.3 + defaultBackground.green * 0.59 + defaultBackground.blue * 0.11);

        int adjustedRed;
        int adjustedGreen;
        int adjustedBlue;

        if (grayscale > 128) { // if closer to white
            adjustedRed = Math.max(0, defaultBackground.red - TINT);
            adjustedGreen = Math.max(0, defaultBackground.green - TINT);
            adjustedBlue = Math.max(0, defaultBackground.blue - TINT);
        } else { // if closer to black
            adjustedRed = Math.min(255, defaultBackground.red + TINT);
            adjustedGreen = Math.min(255, defaultBackground.green + TINT);
            adjustedBlue = Math.min(255, defaultBackground.blue + TINT);
        }

        return new RGB(adjustedRed, adjustedGreen, adjustedBlue);
    }

    public RGB getHighlight() {
        ITheme theme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
        RGB modifiedColor = theme.getColorRegistry().getRGBValue("org.eclipse.jdt.ui.error.foreground");
        RGB defaultForeground = theme.getColorRegistry().getRGBValue("org.eclipse.ui.workbench.foreground");

        return modifiedColor != null ? modifiedColor : defaultForeground;
    }

    public java.util.Map<String, String> getTheme() {
        Display display = Display.getDefault();

        ITheme theme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
        Color background = display.getSystemColor(org.eclipse.swt.SWT.COLOR_WIDGET_BACKGROUND);
        Color foreground = display.getSystemColor(org.eclipse.swt.SWT.COLOR_WIDGET_FOREGROUND);

        Color buttonBackground = display.getSystemColor(org.eclipse.swt.SWT.COLOR_LIST_BACKGROUND);
        Color buttonForeground = display.getSystemColor(org.eclipse.swt.SWT.COLOR_LIST_FOREGROUND);

        Color badgeBackground = display.getSystemColor(org.eclipse.swt.SWT.COLOR_LIST_BACKGROUND);
        Color badgeForeground = display.getSystemColor(org.eclipse.swt.SWT.COLOR_LIST_FOREGROUND);

        Color inputBackground = display.getSystemColor(org.eclipse.swt.SWT.COLOR_LIST_BACKGROUND);

        Color border = display.getSystemColor(org.eclipse.swt.SWT.COLOR_WIDGET_NORMAL_SHADOW);
        Color focusBorder = new Color(display, new RGB(100, 100, 255));

        RGB editorBackground = theme.getColorRegistry().getRGBValue("org.eclipse.ui.workbench.background");
        RGB editorForeground = theme.getColorRegistry().getRGBValue("org.eclipse.ui.workbench.foreground");

        RGB actionHoverBackground = theme.getColorRegistry().getRGBValue("org.eclipse.ui.workbench.hover.background");

        RGB findMatchBackground = theme.getColorRegistry().getRGBValue("org.eclipse.search.lowlightmatch.background");
        if (findMatchBackground == null) {
            findMatchBackground = new RGB(255, 221, 0);
        }

        java.util.Map<String, String> themeMap = new java.util.HashMap<>();

        themeMap.put("--vscode-editor-foreground", toHex(editorForeground));
        themeMap.put("--vscode-editor-background", toHex(editorBackground));

        themeMap.put("--vscode-button-background", toHex(buttonBackground.getRGB()));
        themeMap.put("--vscode-button-foreground", toHex(buttonForeground.getRGB()));

        themeMap.put("--vscode-list-activeSelectionBackground", toHex(actionHoverBackground) + "50");

        themeMap.put("--vscode-quickInputList-focusForeground", toHex(foreground.getRGB()));
        themeMap.put("--vscode-quickInput-background", toHex(inputBackground.getRGB()));

        themeMap.put("--vscode-badge-background", toHex(badgeBackground.getRGB()));
        themeMap.put("--vscode-badge-foreground", toHex(badgeForeground.getRGB()));

        themeMap.put("--vscode-input-background", toHex(inputBackground.getRGB()));
        themeMap.put("--vscode-input-border", toHex(border.getRGB()));
        themeMap.put("--vscode-sideBar-background", toHex(background.getRGB()));
        themeMap.put("--vscode-sideBar-border", toHex(border.getRGB()));
        themeMap.put("--vscode-focusBorder", toHex(focusBorder.getRGB()));

        themeMap.put("--vscode-commandCenter-activeBorder", toHex(focusBorder.getRGB()));
        themeMap.put("--vscode-commandCenter-inactiveBorder", toHex(border.getRGB()));

        themeMap.put("--vscode-editor-findMatchHighlightBackground", toHex(findMatchBackground) + "40");

        focusBorder.dispose(); // 사용한 자원 해제

        return themeMap;
    }

    private String toHex(RGB color) {
        return String.format("#%02x%02x%02x", color.red, color.green, color.blue);
    }
}