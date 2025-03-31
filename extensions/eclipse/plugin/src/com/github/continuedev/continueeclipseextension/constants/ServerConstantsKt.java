package com.github.continuedev.continueeclipseextension.constants;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ServerConstantsKt {
   @NotNull
   public static final String DEFAULT_CONFIG = "\n{\n  \"models\": [\n    {\n      \"model\": \"claude-3-5-sonnet-latest\",\n      \"provider\": \"anthropic\",\n      \"apiKey\": \"\",\n      \"title\": \"Claude 3.5 Sonnet\"\n    }\n  ],\n  \"tabAutocompleteModel\": {\n    \"title\": \"Codestral\",\n    \"provider\": \"mistral\",\n    \"model\": \"codestral-latest\",\n    \"apiKey\": \"\" \n  },\n  \"customCommands\": [\n    {\n      \"name\": \"test\",\n      \"prompt\": \"{{{ input }}}\\n\\nWrite a comprehensive set of unit tests for the selected code. It should setup, run tests that check for correctness including important edge cases, and teardown. Ensure that the tests are complete and sophisticated. Give the tests just as chat output, don't edit any file.\",\n      \"description\": \"Write unit tests for highlighted code\"\n    }\n  ],\n  \"contextProviders\": [\n    {\n      \"name\": \"diff\",\n      \"params\": {}\n    },\n    {\n      \"name\": \"folder\",\n      \"params\": {}\n    },\n    {\n      \"name\": \"codebase\",\n      \"params\": {}\n    }\n  ],\n  \"slashCommands\": [\n    {\n      \"name\": \"share\",\n      \"description\": \"Export the current chat session to markdown\"\n    },\n    {\n      \"name\": \"commit\",\n      \"description\": \"Generate a git commit message\"\n    }\n  ],\n  \"docs\": []\n}\n";
   @NotNull
   public static final String DEFAULT_CONFIG_JS = "\nfunction modifyConfig(config) {\n  return config;\n}\nexport {\n  modifyConfig\n};\n";

   @NotNull
   public static final String getContinueGlobalPath() {
      String var10000 = System.getProperty("user.home");
      String[] var1 = new String[]{".continue"};
      Path continuePath = Paths.get(var10000, var1);
      if (Files.notExists(continuePath, new LinkOption[0])) {
         Files.createDirectories(continuePath);
      }

      return continuePath.toString();
   }

   @NotNull
   public static final String getContinueRemoteConfigPath(@NotNull String remoteHostname) {
      Intrinsics.checkNotNullParameter(remoteHostname, "remoteHostname");
      String var10000 = getContinueGlobalPath();
      String[] var2 = new String[]{".configs"};
      Path path = Paths.get(var10000, var2);
      if (Files.notExists(path, new LinkOption[0])) {
         Files.createDirectories(path);
      }

      var10000 = path.toString();
      var2 = new String[]{remoteHostname};
      return Paths.get(var10000, var2).toString();
   }

   @NotNull
   public static final String getConfigJsonPath(@Nullable String remoteHostname) {
      String var10000 = remoteHostname != null ? getContinueRemoteConfigPath(remoteHostname) : getContinueGlobalPath();
      String[] var2 = new String[]{"config.json"};
      Path path = Paths.get(var10000, var2);
      if (Files.notExists(path, new LinkOption[0])) {
         Files.createFile(path);
         Files.writeString(path, (CharSequence)(remoteHostname == null ? "\n{\n  \"models\": [\n    {\n      \"model\": \"claude-3-5-sonnet-latest\",\n      \"provider\": \"anthropic\",\n      \"apiKey\": \"\",\n      \"title\": \"Claude 3.5 Sonnet\"\n    }\n  ],\n  \"tabAutocompleteModel\": {\n    \"title\": \"Codestral\",\n    \"provider\": \"mistral\",\n    \"model\": \"codestral-latest\",\n    \"apiKey\": \"\" \n  },\n  \"customCommands\": [\n    {\n      \"name\": \"test\",\n      \"prompt\": \"{{{ input }}}\\n\\nWrite a comprehensive set of unit tests for the selected code. It should setup, run tests that check for correctness including important edge cases, and teardown. Ensure that the tests are complete and sophisticated. Give the tests just as chat output, don't edit any file.\",\n      \"description\": \"Write unit tests for highlighted code\"\n    }\n  ],\n  \"contextProviders\": [\n    {\n      \"name\": \"diff\",\n      \"params\": {}\n    },\n    {\n      \"name\": \"folder\",\n      \"params\": {}\n    },\n    {\n      \"name\": \"codebase\",\n      \"params\": {}\n    }\n  ],\n  \"slashCommands\": [\n    {\n      \"name\": \"share\",\n      \"description\": \"Export the current chat session to markdown\"\n    },\n    {\n      \"name\": \"commit\",\n      \"description\": \"Generate a git commit message\"\n    }\n  ],\n  \"docs\": []\n}\n" : "{}"));
      }

      return path.toString();
   }

   // $FF: synthetic method
   public static String getConfigJsonPath$default(String var0, int var1, Object var2) {
      if ((var1 & 1) != 0) {
         var0 = null;
      }

      return getConfigJsonPath(var0);
   }

   @NotNull
   public static final String getConfigJsPath(@Nullable String remoteHostname) {
      String var10000 = remoteHostname != null ? getContinueRemoteConfigPath(remoteHostname) : getContinueGlobalPath();
      String[] var2 = new String[]{"config.js"};
      Path path = Paths.get(var10000, var2);
      if (Files.notExists(path, new LinkOption[0])) {
         Files.createFile(path);
         Files.writeString(path, (CharSequence)"\nfunction modifyConfig(config) {\n  return config;\n}\nexport {\n  modifyConfig\n};\n");
      }

      return path.toString();
   }

   // $FF: synthetic method
   public static String getConfigJsPath$default(String var0, int var1, Object var2) {
      if ((var1 & 1) != 0) {
         var0 = null;
      }

      return getConfigJsPath(var0);
   }

   @NotNull
   public static final String getSessionsDir() {
      String var10000 = getContinueGlobalPath();
      String[] var1 = new String[]{"sessions"};
      Path path = Paths.get(var10000, var1);
      if (Files.notExists(path, new LinkOption[0])) {
         Files.createDirectories(path);
      }

      return path.toString();
   }

   @NotNull
   public static final String getSessionsListPath() {
      String var10000 = getSessionsDir();
      String[] var1 = new String[]{"sessions.json"};
      Path path = Paths.get(var10000, var1);
      if (Files.notExists(path, new LinkOption[0])) {
         Files.createFile(path);
         Files.writeString(path, (CharSequence)"[]");
      }

      return path.toString();
   }

   @NotNull
   public static final String getSessionFilePath(@NotNull String sessionId) {
      Intrinsics.checkNotNullParameter(sessionId, "sessionId");
      String var10000 = getSessionsDir();
      String[] var2 = new String[]{sessionId + ".json"};
      Path path = Paths.get(var10000, var2);
      if (Files.notExists(path, new LinkOption[0])) {
         Files.createFile(path);
         Files.writeString(path, (CharSequence)"{}");
      }

      return path.toString();
   }
}
