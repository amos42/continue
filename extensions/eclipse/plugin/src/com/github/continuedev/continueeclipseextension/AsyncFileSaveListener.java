package com.github.continuedev.continueeclipseextension;

import java.util.Arrays;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.runtime.CoreException;
import com.github.continuedev.continueeclipsejextension.services.ContinuePluginService;

public class AsyncFileSaveListener implements IResourceChangeListener {
    
    private final ContinuePluginService continuePluginService;
    private final List<String> configFilePatterns = Arrays.asList(
        ".continue/config.json",
        ".continue/config.ts",
        ".continue/config.yaml",
        ".continuerc.json"
    );

    public AsyncFileSaveListener(ContinuePluginService continuePluginService) {
        this.continuePluginService = continuePluginService;
    }

    @Override
    public void resourceChanged(IResourceChangeEvent event) {
        try {
            if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
                event.getDelta().accept(new IResourceDeltaVisitor() {
                    @Override
                    public boolean visit(IResourceDelta delta) {
                        if (delta.getResource() instanceof IFile) {
                            IFile file = (IFile) delta.getResource();
                            if (configFilePatterns.stream().anyMatch(pattern -> file.getFullPath().toOSString().endsWith(pattern))) {
                                continuePluginService.coreMessenger.request("config/reload", null, null, _ -> {});
                                return false; // 더 이상 하위 리소스 방문하지 않음
                            }
                        }
                        return true; // 하위 리소스 방문 계속
                    }
                });
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }
}