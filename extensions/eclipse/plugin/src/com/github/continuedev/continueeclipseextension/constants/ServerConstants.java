package com.github.continuedev.continueeclipseextension.constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerConstants {
    public static final String DEFAULT_CONFIG =
"""
{
  "models": [
    {
      "model": "claude-3-5-sonnet-latest",
      "provider": "anthropic",
      "apiKey": "",
      "title": "Claude 3.5 Sonnet"
    }
  ],
  "tabAutocompleteModel": {
    "title": "Codestral",
    "provider": "mistral",
    "model": "codestral-latest",
    "apiKey": ""
  },
  "customCommands": [
    {
      "name": "test",
      "prompt": "{{{ input }}}\n\nWrite a comprehensive set of unit tests for the selected code. It should setup, run tests that check for correctness including important edge cases, and teardown. Ensure that the tests are complete and sophisticated. Give the tests just as chat output, don't edit any file.",
      "description": "Write unit tests for highlighted code"
    }
  ],
  "contextProviders": [
    {
      "name": "diff",
      "params": {}
    },
    {
      "name": "folder",
      "params": {}
    },
    {
      "name": "codebase",
      "params": {}
    }
  ],
  "slashCommands": [
    {
      "name": "share",
      "description": "Export the current chat session to markdown"
    },
    {
      "name": "commit",
      "description": "Generate a git commit message"
    }
  ],
  "docs": []
}
""";

 public static final String DEFAULT_CONFIG_JS =
"""
function modifyConfig(config) {
    return config;
}
exports.modifyConfig = modifyConfig;
""";

    public static String getContinueGlobalPath() {
        String continuePath = Paths.get(System.getProperty("user.home"), ".continue").toString();
        if (!Files.exists(Path.of(continuePath))) {
            try {
				Files.createDirectories(Path.of(continuePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return continuePath;
    }

    public static String getContinueRemoteConfigPath(String remoteHostname) {
        String path = Paths.get(getContinueGlobalPath(), ".configs", remoteHostname).toString();
        if (!Files.exists(Path.of(path))) {
            try {
				Files.createDirectories(Path.of(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return path;
    }

    public static String getConfigJsonPath(String remoteHostname) {
        Path path = Paths.get(
                (remoteHostname != null) ? getContinueRemoteConfigPath(remoteHostname) : getContinueGlobalPath(),
                "config.json"
        );
        if (!Files.exists(path)) {
            try {
				Files.createFile(path);
	            Files.writeString(path, DEFAULT_CONFIG);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return path.toString();
    }

    public static String getConfigJsPath(String remoteHostname) {
        Path path = Paths.get(
                (remoteHostname != null) ? getContinueRemoteConfigPath(remoteHostname) : getContinueGlobalPath(),
                "config.js"
        );
        if (!Files.exists(path)) {
            try {
				Files.createFile(path);
	            Files.writeString(path, DEFAULT_CONFIG_JS);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return path.toString();
    }

    public static String getSessionsDir() {
        Path path = Paths.get(getContinueGlobalPath(), "sessions");
        if (!Files.exists(path)) {
            try {
				Files.createDirectories(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return path.toString();
    }

    public static String getSessionsListPath() {
    	Path path = Paths.get(getSessionsDir(), "sessions.json");
        if (!Files.exists(path)) {
            try {
				Files.createFile(path);
	            Files.writeString(path, "[]");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return path.toString();
    }

    public static String getSessionFilePath(String sessionId) {
    	Path path = Paths.get(getSessionsDir(), "$sessionId.json");
        if (!Files.exists(path)) {
            try {
				Files.createFile(path);
	            Files.writeString(path, "{}");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return path.toString();
    }
}
