package com.github.continuedev.continueeclipseextension.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ContinueExtensionConfigurable implements Configurable {
   @Nullable
   private ContinueSettingsComponent mySettingsComponent;

   @NotNull
   public JComponent createComponent() {
      this.mySettingsComponent = new ContinueSettingsComponent();
      ContinueSettingsComponent var10000 = this.mySettingsComponent;
      Intrinsics.checkNotNull(var10000);
      return (JComponent)var10000.getPanel();
   }

   public boolean isModified() {
      ContinueExtensionSettings settings;
      String var10000;
      label116: {
         settings = ContinueExtensionSettings.Companion.getInstance();
         ContinueSettingsComponent var3 = this.mySettingsComponent;
         if (var3 != null) {
            JTextField var4 = var3.getRemoteConfigServerUrl();
            if (var4 != null) {
               var10000 = var4.getText();
               break label116;
            }
         }

         var10000 = null;
      }

      label111: {
         if (Intrinsics.areEqual(var10000, settings.getContinueState().getRemoteConfigServerUrl())) {
            label108: {
               ContinueSettingsComponent var6 = this.mySettingsComponent;
               if (var6 != null) {
                  JTextField var13 = var6.getRemoteConfigSyncPeriod();
                  if (var13 != null) {
                     String var5 = var13.getText();
                     if (var5 != null) {
                        var20 = Integer.parseInt(var5) == settings.getContinueState().getRemoteConfigSyncPeriod();
                        break label108;
                     }
                  }
               }

               var20 = false;
            }

            if (var20) {
               label101: {
                  ContinueSettingsComponent var7 = this.mySettingsComponent;
                  if (var7 != null) {
                     JTextField var14 = var7.getUserToken();
                     if (var14 != null) {
                        var10000 = var14.getText();
                        break label101;
                     }
                  }

                  var10000 = null;
               }

               if (Intrinsics.areEqual(var10000, settings.getContinueState().getUserToken())) {
                  label95: {
                     ContinueSettingsComponent var8 = this.mySettingsComponent;
                     if (var8 != null) {
                        JCheckBox var15 = var8.getEnableTabAutocomplete();
                        if (var15 != null) {
                           var22 = var15.isSelected() == settings.getContinueState().getEnableTabAutocomplete();
                           break label95;
                        }
                     }

                     var22 = false;
                  }

                  if (var22) {
                     label89: {
                        ContinueSettingsComponent var9 = this.mySettingsComponent;
                        if (var9 != null) {
                           JCheckBox var16 = var9.getEnableContinueTeamsBeta();
                           if (var16 != null) {
                              var23 = var16.isSelected() == settings.getContinueState().getEnableContinueTeamsBeta();
                              break label89;
                           }
                        }

                        var23 = false;
                     }

                     if (var23) {
                        label83: {
                           ContinueSettingsComponent var10 = this.mySettingsComponent;
                           if (var10 != null) {
                              JCheckBox var17 = var10.getEnableOSR();
                              if (var17 != null) {
                                 var24 = var17.isSelected() == settings.getContinueState().getEnableOSR();
                                 break label83;
                              }
                           }

                           var24 = false;
                        }

                        if (var24) {
                           label77: {
                              ContinueSettingsComponent var11 = this.mySettingsComponent;
                              if (var11 != null) {
                                 JCheckBox var18 = var11.getDisplayEditorTooltip();
                                 if (var18 != null) {
                                    var25 = var18.isSelected() == settings.getContinueState().getDisplayEditorTooltip();
                                    break label77;
                                 }
                              }

                              var25 = false;
                           }

                           if (var25) {
                              label71: {
                                 ContinueSettingsComponent var12 = this.mySettingsComponent;
                                 if (var12 != null) {
                                    JCheckBox var19 = var12.getShowIDECompletionSideBySide();
                                    if (var19 != null) {
                                       var26 = var19.isSelected() == settings.getContinueState().getShowIDECompletionSideBySide();
                                       break label71;
                                    }
                                 }

                                 var26 = false;
                              }

                              if (var26) {
                                 var27 = false;
                                 break label111;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         var27 = true;
      }

      boolean modified = var27;
      return modified;
   }

   public void apply() {
      ContinueExtensionSettings settings;
      ContinueExtensionSettings.ContinueState var10000;
      String var10001;
      label77: {
         settings = ContinueExtensionSettings.Companion.getInstance();
         var10000 = settings.getContinueState();
         ContinueSettingsComponent var2 = this.mySettingsComponent;
         if (var2 != null) {
            JTextField var3 = var2.getRemoteConfigServerUrl();
            if (var3 != null) {
               var10001 = var3.getText();
               break label77;
            }
         }

         var10001 = null;
      }

      label72: {
         var10000.setRemoteConfigServerUrl(var10001);
         var10000 = settings.getContinueState();
         ContinueSettingsComponent var6 = this.mySettingsComponent;
         if (var6 != null) {
            JTextField var13 = var6.getRemoteConfigSyncPeriod();
            if (var13 != null) {
               String var4 = var13.getText();
               if (var4 != null) {
                  int var5 = Integer.parseInt(var4);
                  var32 = var5;
                  break label72;
               }
            }
         }

         var32 = 60;
      }

      label66: {
         var10000.setRemoteConfigSyncPeriod(var32);
         var10000 = settings.getContinueState();
         ContinueSettingsComponent var7 = this.mySettingsComponent;
         if (var7 != null) {
            JTextField var14 = var7.getUserToken();
            if (var14 != null) {
               var10001 = var14.getText();
               break label66;
            }
         }

         var10001 = null;
      }

      label61: {
         var10000.setUserToken(var10001);
         var10000 = settings.getContinueState();
         ContinueSettingsComponent var8 = this.mySettingsComponent;
         if (var8 != null) {
            JCheckBox var15 = var8.getEnableTabAutocomplete();
            if (var15 != null) {
               boolean var20 = var15.isSelected();
               var34 = var20;
               break label61;
            }
         }

         var34 = false;
      }

      label56: {
         var10000.setEnableTabAutocomplete(var34);
         var10000 = settings.getContinueState();
         ContinueSettingsComponent var9 = this.mySettingsComponent;
         if (var9 != null) {
            JCheckBox var16 = var9.getEnableContinueTeamsBeta();
            if (var16 != null) {
               boolean var21 = var16.isSelected();
               var35 = var21;
               break label56;
            }
         }

         var35 = false;
      }

      label51: {
         var10000.setEnableContinueTeamsBeta(var35);
         var10000 = settings.getContinueState();
         ContinueSettingsComponent var10 = this.mySettingsComponent;
         if (var10 != null) {
            JCheckBox var17 = var10.getEnableOSR();
            if (var17 != null) {
               boolean var22 = var17.isSelected();
               var36 = var22;
               break label51;
            }
         }

         var36 = true;
      }

      label46: {
         var10000.setEnableOSR(var36);
         var10000 = settings.getContinueState();
         ContinueSettingsComponent var11 = this.mySettingsComponent;
         if (var11 != null) {
            JCheckBox var18 = var11.getDisplayEditorTooltip();
            if (var18 != null) {
               boolean var23 = var18.isSelected();
               var37 = var23;
               break label46;
            }
         }

         var37 = true;
      }

      label41: {
         var10000.setDisplayEditorTooltip(var37);
         var10000 = settings.getContinueState();
         ContinueSettingsComponent var12 = this.mySettingsComponent;
         if (var12 != null) {
            JCheckBox var19 = var12.getShowIDECompletionSideBySide();
            if (var19 != null) {
               boolean var24 = var19.isSelected();
               var38 = var24;
               break label41;
            }
         }

         var38 = false;
      }

      var10000.setShowIDECompletionSideBySide(var38);
      ((SettingsListener)ApplicationManager.getApplication().getMessageBus().syncPublisher(SettingsListener.Companion.getTOPIC())).settingsUpdated(settings.getContinueState());
      ContinueExtensionSettings.Companion.getInstance().addRemoteSyncJob();
   }

   public void reset() {
      ContinueExtensionSettings settings = ContinueExtensionSettings.Companion.getInstance();
      ContinueSettingsComponent var3 = this.mySettingsComponent;
      JTextField var2 = var3 != null ? var3.getRemoteConfigServerUrl() : null;
      if (var2 != null) {
         var2.setText(settings.getContinueState().getRemoteConfigServerUrl());
      }

      var3 = this.mySettingsComponent;
      var2 = var3 != null ? var3.getRemoteConfigSyncPeriod() : null;
      if (var2 != null) {
         var2.setText(String.valueOf(settings.getContinueState().getRemoteConfigSyncPeriod()));
      }

      var3 = this.mySettingsComponent;
      var2 = var3 != null ? var3.getUserToken() : null;
      if (var2 != null) {
         var2.setText(settings.getContinueState().getUserToken());
      }

      var3 = this.mySettingsComponent;
      JCheckBox var6 = var3 != null ? var3.getEnableTabAutocomplete() : null;
      if (var6 != null) {
         var6.setSelected(settings.getContinueState().getEnableTabAutocomplete());
      }

      var3 = this.mySettingsComponent;
      var6 = var3 != null ? var3.getEnableContinueTeamsBeta() : null;
      if (var6 != null) {
         var6.setSelected(settings.getContinueState().getEnableContinueTeamsBeta());
      }

      var3 = this.mySettingsComponent;
      var6 = var3 != null ? var3.getEnableOSR() : null;
      if (var6 != null) {
         var6.setSelected(settings.getContinueState().getEnableOSR());
      }

      var3 = this.mySettingsComponent;
      var6 = var3 != null ? var3.getDisplayEditorTooltip() : null;
      if (var6 != null) {
         var6.setSelected(settings.getContinueState().getDisplayEditorTooltip());
      }

      var3 = this.mySettingsComponent;
      var6 = var3 != null ? var3.getShowIDECompletionSideBySide() : null;
      if (var6 != null) {
         var6.setSelected(settings.getContinueState().getShowIDECompletionSideBySide());
      }

      ContinueExtensionSettings.Companion.getInstance().addRemoteSyncJob();
   }

   public void disposeUIResources() {
      this.mySettingsComponent = null;
   }

   @NotNull
   public String getDisplayName() {
      return "Continue Extension Settings";
   }
}
