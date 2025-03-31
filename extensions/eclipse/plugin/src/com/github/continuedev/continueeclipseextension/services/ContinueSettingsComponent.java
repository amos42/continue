package com.github.continuedev.continueeclipseextension.services;

import com.intellij.openapi.project.DumbAware;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jetbrains.annotations.NotNull;

public final class ContinueSettingsComponent implements DumbAware {
	@NotNull
	private final JPanel panel = new JPanel((LayoutManager)(new GridBagLayout()));
	@NotNull
	private final JTextField remoteConfigServerUrl = new JTextField();
	@NotNull
	private final JTextField remoteConfigSyncPeriod = new JTextField();
	@NotNull
	private final JTextField userToken = new JTextField();
	@NotNull
	private final JCheckBox enableTabAutocomplete = new JCheckBox("Enable Tab Autocomplete");
	@NotNull
	private final JCheckBox enableContinueTeamsBeta = new JCheckBox("Enable Continue for Teams Beta");
	@NotNull
	private final JCheckBox enableOSR = new JCheckBox("Enable Off-Screen Rendering");
	@NotNull
	private final JCheckBox displayEditorTooltip = new JCheckBox("Display Editor Tooltip");
	@NotNull
	private final JCheckBox showIDECompletionSideBySide = new JCheckBox("Show IDE completions side-by-side");
	
	public ContinueSettingsComponent() {
	   GridBagConstraints constraints = new GridBagConstraints();
	   constraints.fill = 2;
	   constraints.weightx = (double)1.0F;
	   constraints.weighty = (double)0.0F;
	   constraints.gridx = 0;
	   constraints.gridy = -1;
	   this.panel.add((Component)(new JLabel("Remote Config Server URL:")), constraints);
	   int filler = constraints.gridy++;
	   filler = constraints.gridy++;
	   this.panel.add((Component)this.remoteConfigServerUrl, constraints);
	   filler = constraints.gridy++;
	   this.panel.add((Component)(new JLabel("Remote Config Sync Period (in minutes):")), constraints);
	   filler = constraints.gridy++;
	   this.panel.add((Component)this.remoteConfigSyncPeriod, constraints);
	   filler = constraints.gridy++;
	   this.panel.add((Component)(new JLabel("User Token:")), constraints);
	   filler = constraints.gridy++;
	   this.panel.add((Component)this.userToken, constraints);
	   filler = constraints.gridy++;
	   this.panel.add((Component)this.enableTabAutocomplete, constraints);
	   filler = constraints.gridy++;
	   this.panel.add((Component)this.enableContinueTeamsBeta, constraints);
	   filler = constraints.gridy++;
	   this.panel.add((Component)this.enableOSR, constraints);
	   filler = constraints.gridy++;
	   this.panel.add((Component)this.displayEditorTooltip, constraints);
	   filler = constraints.gridy++;
	   this.panel.add((Component)this.showIDECompletionSideBySide, constraints);
	   filler = constraints.gridy++;
	   constraints.weighty = (double)1.0F;
	   JPanel filler = new JPanel();
	   this.panel.add((Component)filler, constraints);
	}
	
	@NotNull
	public final JPanel getPanel() {
	   return this.panel;
	}
	
	@NotNull
	public final JTextField getRemoteConfigServerUrl() {
	   return this.remoteConfigServerUrl;
	}
	
	@NotNull
	public final JTextField getRemoteConfigSyncPeriod() {
	   return this.remoteConfigSyncPeriod;
	}
	
	@NotNull
	public final JTextField getUserToken() {
	   return this.userToken;
	}
	
	@NotNull
	public final JCheckBox getEnableTabAutocomplete() {
	   return this.enableTabAutocomplete;
	}
	
	@NotNull
	public final JCheckBox getEnableContinueTeamsBeta() {
	   return this.enableContinueTeamsBeta;
	}
	
	@NotNull
	public final JCheckBox getEnableOSR() {
	   return this.enableOSR;
	}
	
	@NotNull
	public final JCheckBox getDisplayEditorTooltip() {
	   return this.displayEditorTooltip;
	}
	
	@NotNull
	public final JCheckBox getShowIDECompletionSideBySide() {
	   return this.showIDECompletionSideBySide;
	}
}