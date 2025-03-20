/*
 * Copyright © 2017 jjYBdx4IL (https://github.com/jjYBdx4IL)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.continuedev.continueeclipseextension.utils;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Desktop {
    private static final Logger LOG = LoggerFactory.getLogger(Desktop.class);
    private static Desktop instance;

    private Desktop() {
        // Singleton instance cannot be instantiated
    }

    public static synchronized Desktop getInstance() {
        if (instance == null) {
            instance = new Desktop();
        }
        return instance;
    }

    public boolean browse(URI uri) {
        if (browseDESKTOP(uri)) {
            return true;
        }

        if (openSystemSpecific(uri.toString())) {
            return true;
        }

        LOG.warn("failed to browse {}", uri);
        return false;
    }

    public boolean open(File file) {
        if (openDESKTOP(file)) {
            return true;
        }

        if (openSystemSpecific(file.getPath())) {
            return true;
        }

        LOG.warn("failed to open {}", file.getAbsolutePath());
        return false;
    }

    public boolean edit(File file) {
        if (editDESKTOP(file)) {
            return true;
        }

        if (openSystemSpecific(file.getPath())) {
            return true;
        }

        LOG.warn("failed to edit {}", file.getAbsolutePath());
        return false;
    }

    private boolean openSystemSpecific(String what) {
        if (SystemUtils.IS_OS_LINUX) {
            if (isXDG() && runCommand("xdg-open", "%s", what)) {
                return true;
            }
            if (isKDE() && runCommand("kde-open", "%s", what)) {
                return true;
            }
            if (isGNOME() && runCommand("gnome-open", "%s", what)) {
                return true;
            }
            if (runCommand("kde-open", "%s", what)) {
                return true;
            }
            if (runCommand("gnome-open", "%s", what)) {
                return true;
            }
        }

        if (SystemUtils.IS_OS_MAC && runCommand("open", "%s", what)) {
            return true;
        }

        if (SystemUtils.IS_OS_WINDOWS && runCommand("explorer", "%s", what)) {
            return true;
        }

        return false;
    }

    private boolean browseDESKTOP(URI uri) {
        try {
            if (!Desktop.isDesktopSupported()) {
                LOG.debug("Platform is not supported.");
                return false;
            }

            if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                LOG.debug("BROWSE is not supported.");
                return false;
            }

            LOG.info("Trying to use Desktop.getDesktop().browse() with {}", uri.toString());
            Desktop.getDesktop().browse(uri);
            return true;
        } catch (Throwable t) {
            LOG.error("Error using desktop browse.", t);
            return false;
        }
    }

    private boolean openDESKTOP(File file) {
        try {
            if (!Desktop.isDesktopSupported()) {
                LOG.debug("Platform is not supported.");
                return false;
            }

            if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                LOG.debug("OPEN is not supported.");
                return false;
            }

            LOG.info("Trying to use Desktop.getDesktop().open() with {}", file.toString());
            Desktop.getDesktop().open(file);
            return true;
        } catch (Throwable t) {
            LOG.error("Error using desktop open.", t);
            return false;
        }
    }

    private boolean editDESKTOP(File file) {
        try {
            if (!Desktop.isDesktopSupported()) {
                LOG.debug("Platform is not supported.");
                return false;
            }

            if (!Desktop.getDesktop().isSupported(Desktop.Action.EDIT)) {
                LOG.debug("EDIT is not supported.");
                return false;
            }

            LOG.info("Trying to use Desktop.getDesktop().edit() with {}", file);
            Desktop.getDesktop().edit(file);
            return true;
        } catch (Throwable t) {
            LOG.error("Error using desktop edit.", t);
            return false;
        }
    }

    private boolean runCommand(String command, String args, String file) {
        LOG.info("Trying to exec:\n   cmd = {}\n   args = {}\n   %s = {}", command, args, file);

        String[] parts = prepareCommand(command, args, file);

        try {
            Process p = Runtime.getRuntime().exec(parts);
            if (p == null) {
                return false;
            } else {
                try {
                    int retval = p.exitValue();
                    if (retval == 0) {
                        LOG.error("Process ended immediately.");
                        return false;
                    } else {
                        LOG.error("Process crashed.");
                        return false;
                    }
                } catch (IllegalThreadStateException itse) {
                    LOG.error("Process is running.");
                    return true;
                }
            }
        } catch (IOException e) {
            LOG.error("Error running command.", e);
            return false;
        }
    }

    private String[] prepareCommand(String command, String args, String file) {
        java.util.List<String> parts = new java.util.ArrayList<>();
        parts.add(command);

        if (args != null) {
            String[] splitArgs = args.split(" ");
            for (String s : splitArgs) {
                parts.add(String.format(s, file).trim());
            }
        }

        return parts.toArray(new String[0]);
    }

    private boolean isXDG() {
        String xdgSessionId = System.getenv("XDG_SESSION_ID");
        return xdgSessionId != null && !xdgSessionId.isEmpty();
    }

    private boolean isGNOME() {
        String gdmSession = System.getenv("GDMSESSION");
        return gdmSession != null && gdmSession.toLowerCase().contains("gnome");
    }

    private boolean isKDE() {
        String gdmSession = System.getenv("GDMSESSION");
        return gdmSession != null && gdmSession.toLowerCase().contains("kde");
    }
}