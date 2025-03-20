package com.github.continuedev.continueeclipseextension.factories;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefCallback;
import org.cef.callback.CefSchemeHandlerFactory;
import org.cef.handler.CefLoadHandler;
import org.cef.handler.CefResourceHandler;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public class CustomSchemeHandlerFactory implements CefSchemeHandlerFactory {
    @Override
    public CefResourceHandler create(CefBrowser browser, CefFrame frame, String schemeName, CefRequest request) {
        return new CustomResourceHandler();
    }
}

class CustomResourceHandler implements CefResourceHandler {
    private ResourceHandlerState state = ClosedConnection.INSTANCE;
    private String currentUrl = null;

    @Override
    public boolean processRequest(CefRequest cefRequest, CefCallback cefCallback) {
        String url = cefRequest.getUrl();
        if (url != null) {
            String pathToResource = url.replace("http://continue", "webview/").replace("http://localhost:5173", "webview/");
            InputStream newUrl = this.getClass().getClassLoader().getResourceAsStream(pathToResource);
            URLConnection connection = newUrl != null ? this.getClass().getClassLoader().getResource(pathToResource).openConnection() : null;
            state = new OpenedConnection(connection);
            currentUrl = url;
            cefCallback.Continue();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl) {
        if (currentUrl != null) {
            if (currentUrl.contains("css")) {
                cefResponse.setMimeType("text/css");
            } else if (currentUrl.contains("js")) {
                cefResponse.setMimeType("text/javascript");
            } else if (currentUrl.contains("html")) {
                cefResponse.setMimeType("text/html");
            }
        }
        state.getResponseHeaders(cefResponse, responseLength, redirectUrl);
    }

    @Override
    public boolean readResponse(byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback) {
        return state.readResponse(dataOut, bytesToRead, bytesRead, callback);
    }

    @Override
    public void cancel() {
        state.close();
        state = ClosedConnection.INSTANCE;
    }
}

abstract class ResourceHandlerState {
    public abstract void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl);

    public boolean readResponse(byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback) {
        return false;
    }

    public abstract void close();
}

class OpenedConnection extends ResourceHandlerState {
    private URLConnection connection;
    private InputStream inputStream = null;

    public OpenedConnection(URLConnection connection) {
        this.connection = connection;
    }

    @Override
    public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl) {
        try {
            if (connection != null) {
                String fullUrl = connection.getURL().toString();
                // Extract only the resource path after the JAR prefix to prevent incorrect mime type matching
                // (e.g., if a user's home folder contains "js" in the path)
                String url = fullUrl.substring(fullUrl.lastIndexOf("!/") + 2, fullUrl.length());
                if (url.contains("css")) {
                    cefResponse.setMimeType("text/css");
                } else if (url.contains("js")) {
                    cefResponse.setMimeType("text/javascript");
                } else if (url.contains("html")) {
                    cefResponse.setMimeType("text/html");
                } else {
                    cefResponse.setMimeType(connection.getContentType());
                }
                inputStream = connection.getInputStream();
                responseLength.set(inputStream.available());
                cefResponse.setStatus(200);
            } else {
                // Handle the case where connection is null
                cefResponse.setError(CefLoadHandler.ErrorCode.ERR_FAILED);
                cefResponse.setStatusText("Connection is null");
                cefResponse.setStatus(500);
            }
        } catch (IOException e) {
            cefResponse.setError(CefLoadHandler.ErrorCode.ERR_FILE_NOT_FOUND);
            cefResponse.setStatusText(e.getLocalizedMessage());
            cefResponse.setStatus(404);
        }
    }

    @Override
    public boolean readResponse(byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback) {
        if (inputStream != null) {
            try {
                int availableSize = inputStream.available();
                if (availableSize > 0) {
                    int maxBytesToRead = Math.min(availableSize, bytesToRead);
                    int realBytesRead = inputStream.read(dataOut, 0, maxBytesToRead);
                    bytesRead.set(realBytesRead);
                    return true;
                } else {
                    inputStream.close();
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void close() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

enum ClosedConnection implements ResourceHandlerState {
    INSTANCE;

    @Override
    public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl) {
        cefResponse.setStatus(404);
    }

    @Override
    public void close() {}
}