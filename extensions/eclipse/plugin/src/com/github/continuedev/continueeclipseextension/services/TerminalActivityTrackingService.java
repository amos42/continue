package com.github.continuedev.continueeclipseextension.services;

//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.custom.StyledText;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.eclipse.ui.services.IDisposable;
//
//public class TerminalActivityTrackingService implements IDisposable {
//    private static final List<StyledText> activeTerminalWidgets = new ArrayList<>();
//
//    public TerminalActivityTrackingService() {
//        // Eclipse 터미널이 있을 때마다 포커스 이벤트를 추가해야 합니다.
//        // 예시에서는 Display 리스너를 추가하여 키보드 포커스를 확인합니다.
//        Display display = Display.getDefault();
//        display.addListener(SWT.KeyDown, new Listener() {
//            @Override
//            public void handleEvent(Event event) {
//                Control control = Display.getDefault().getFocusControl();
//                if (control instanceof StyledText) {
//                    StyledText styledText = (StyledText) control;
//                    if (!activeTerminalWidgets.contains(styledText)) {
//                        activeTerminalWidgets.add(styledText);
//                    } else {
//                        activeTerminalWidgets.remove(styledText);
//                        activeTerminalWidgets.add(styledText);
//                    }
//                }
//            }
//        });
//    }
//
//    public void update(List<StyledText> widgets) {
//        synchronized (activeTerminalWidgets) {
//            activeTerminalWidgets.retainAll(widgets);
//            activeTerminalWidgets.removeIf(widget -> !widget.isDisposed() && (widget.isShowing() && widget.isFocusControl()));
//            if (!widgets.isEmpty()) {
//                StyledText widget = widgets.get(0);
//                if (activeTerminalWidgets.isEmpty() || !activeTerminalWidgets.get(activeTerminalWidgets.size() - 1).equals(widget)) {
//                    activeTerminalWidgets.remove(widget);
//                    activeTerminalWidgets.add(widget);
//                }
//            }
//        }
//    }
//
//    public StyledText latest() {
//        synchronized (activeTerminalWidgets) {
//            return activeTerminalWidgets.isEmpty() ? null : activeTerminalWidgets.get(activeTerminalWidgets.size() - 1);
//        }
//    }
//
//    @Override
//    public void dispose() {
//        // 리스너 제거 및 자원 정리
//        Display display = Display.getDefault();
//        display.removeFilter(SWT.KeyDown, event -> {
//            Control control = Display.getDefault().getFocusControl();
//            if (control instanceof StyledText) {
//                StyledText styledText = (StyledText) control;
//                if (activeTerminalWidgets.contains(styledText)) {
//                    activeTerminalWidgets.remove(styledText);
//                }
//            }
//        });
//    }
//}