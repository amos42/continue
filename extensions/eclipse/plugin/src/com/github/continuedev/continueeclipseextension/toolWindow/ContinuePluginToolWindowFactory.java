package com.github.continuedev.continueeclipseextension.toolWindow;

import com.github.continuedev.continueintellijextension.services.ContinuePluginService;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class ContinuePluginToolWindowFactory implements ToolWindowFactory, DumbAware {
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
	   Intrinsics.checkNotNullParameter(project, "project");
	   Intrinsics.checkNotNullParameter(toolWindow, "toolWindow");
	   ContinuePluginWindow continueToolWindow = new ContinuePluginWindow(project);
	   Content titleActions = ContentFactory.getInstance().createContent(continueToolWindow.getContent(), (String)null, false);
	   Intrinsics.checkNotNullExpressionValue(titleActions, "createContent(...)");
	   toolWindow.getContentManager().addContent(titleActions);
	   List titleActions = (List)(new ArrayList());
	   this.createTitleActions(titleActions);
	   AnAction action = ActionManager.getInstance().getAction("MaximizeToolWindow");
	   if (action != null) {
	      titleActions.add(action);
	   }
	
	   toolWindow.setTitleActions(titleActions);
	}
	
	private final void createTitleActions(List titleActions) {
	   AnAction action = ActionManager.getInstance().getAction("ContinueSidebarActionsGroup");
	   if (action != null) {
	      titleActions.add(action);
	   }
	
	}
	
	public boolean shouldBeAvailable(@NotNull Project project) {
	   Intrinsics.checkNotNullParameter(project, "project");
	   return true;
	}
	
	@Metadata(
	   mv = {1, 9, 0},
	   k = 1,
	   xi = 48,
	   d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001b\u0010\u0005\u001a\u00020\u00068FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u000b\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082D¢\u0006\u0002\n\u0000¨\u0006\u0011"},
	   d2 = {"Lcom/github/continuedev/continueintellijextension/toolWindow/ContinuePluginToolWindowFactory$ContinuePluginWindow;", "", "project", "Lcom/intellij/openapi/project/Project;", "(Lcom/intellij/openapi/project/Project;)V", "browser", "Lcom/github/continuedev/continueintellijextension/toolWindow/ContinueBrowser;", "getBrowser", "()Lcom/github/continuedev/continueintellijextension/toolWindow/ContinueBrowser;", "browser$delegate", "Lkotlin/Lazy;", "content", "Ljavax/swing/JComponent;", "getContent", "()Ljavax/swing/JComponent;", "defaultGUIUrl", "", "continue-intellij-extension"}
	)
	public static final class ContinuePluginWindow {
	   @NotNull
	   private final String defaultGUIUrl;
	   @NotNull
	   private final Lazy browser$delegate;
	
	   public ContinuePluginWindow(@NotNull final Project project) {
	      Intrinsics.checkNotNullParameter(project, "project");
	      super();
	      this.defaultGUIUrl = "http://continue/index.html";
	      this.browser$delegate = LazyKt.lazy(new Function0() {
	         public final ContinueBrowser invoke() {
	            String var10000;
	            label12: {
	               String browser = System.getenv("GUI_URL");
	               if (browser != null) {
	                  String continuePluginService = browser.toString();
	                  if (continuePluginService != null) {
	                     var10000 = continuePluginService;
	                     break label12;
	                  }
	               }
	
	               var10000 = ContinuePluginWindow.this.defaultGUIUrl;
	            }
	
	            String url = var10000;
	            ContinueBrowser browser = new ContinueBrowser(project, url);
	            ContinuePluginService continuePluginService = (ContinuePluginService)ServiceManager.getService(project, ContinuePluginService.class);
	            continuePluginService.setContinuePluginWindow(ContinuePluginWindow.this);
	            return browser;
	         }
	
	         // $FF: synthetic method
	         // $FF: bridge method
	         public Object invoke() {
	            return this.invoke();
	         }
	      });
	   }
	
	   @NotNull
	   public final ContinueBrowser getBrowser() {
	      Lazy var1 = this.browser$delegate;
	      Object var2 = null;
	      return (ContinueBrowser)var1.getValue();
	   }
	
	   @NotNull
	   public final JComponent getContent() {
	      JComponent var1 = this.getBrowser().getBrowser().getComponent();
	      Intrinsics.checkNotNullExpressionValue(var1, "getComponent(...)");
	      return var1;
	   }
	}
}
