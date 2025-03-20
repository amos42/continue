package com.github.continuedev.continueeclipseextension.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.SWTKeyLookup;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

public class Utils {

    public enum OS {
        MAC, WINDOWS, LINUX
    }

    public static int getMetaKey() {
        OS os = getOS();
        switch (os) {
            case MAC:
                return KeyStroke.META;
            case WINDOWS:
            case LINUX:
                return KeyStroke.CONTROL;
            default:
                return KeyStroke.CONTROL;
        }
    }

    public static OS getOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac") || osName.contains("darwin")) {
            return OS.MAC;
        } else if (osName.contains("win")) {
            return OS.WINDOWS;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return OS.LINUX;
        } else {
            return OS.LINUX;
        }
    }

    public static String getMetaKeyLabel() {
        OS os = getOS();
        switch (os) {
            case MAC:
                return "⌘";
            case WINDOWS:
            case LINUX:
                return "^";
            default:
                return "^";
        }
    }

    public static String getAltKeyLabel() {
        OS os = getOS();
        switch (os) {
            case MAC:
                return "⌥";
            case WINDOWS:
            case LINUX:
                return "Alt";
            default:
                return "Alt";
        }
    }

    public static String getShiftKeyLabel() {
        OS os = getOS();
        switch (os) {
            case MAC:
                return "⇧";
            case WINDOWS:
            case LINUX:
                return "↑";
            default:
                return "↑";
        }
    }

    public static String getMachineUniqueID() {
        StringBuilder sb = new StringBuilder();
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            return "No MAC Address Found";
        }

        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            byte[] mac;
            try {
                mac = networkInterface.getHardwareAddress();
            } catch (SocketException e) {
                e.printStackTrace();
                continue;
            }

            if (mac != null) {
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                return sb.toString();
            }
        }
        return "No MAC Address Found";
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String toUriOrNull(IFile virtualFile) {
        if (virtualFile == null) {
            return null;
        }
        return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(virtualFile.getLocationURI().getPath())).getRawLocationURI().toString().replaceAll("/$", "");
    }
}