package com.github.continuedev.continueeclipseextension;

import com.github.continuedev.continueeclipseextension.activities.ContinuePluginStartupActivity;
import com.github.continuedev.continueeclipseextension.constants.getContinueGlobalPath;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider;
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory;
import com.jetbrains.jsonSchema.extension.SchemaType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class ConfigJsonSchemaProviderFactory implements JsonSchemaProviderFactory {
    @Override
    public java.util.List<JsonSchemaFileProvider> getProviders(Project project) {
        java.util.List<JsonSchemaFileProvider> providers = new java.util.ArrayList<>();
        providers.add(new ConfigJsonSchemaFileProvider());
        return providers;
    }
}

class ConfigJsonSchemaFileProvider implements JsonSchemaFileProvider {
    @Override
    public boolean isAvailable(VirtualFile file) {
        return file.getPath().endsWith("/.continue/config.json") || file.getPath().endsWith("\\.continue\\config.json");
    }

    @Override
    public String getName() {
        return "config.json";
    }

    @Override
    public VirtualFile getSchemaFile() {
        try {
            java.io.InputStream inputStream = ContinuePluginStartupActivity.class.getClassLoader().getResourceAsStream("config_schema.json");
            if (inputStream == null) {
                throw new IOException("Resource not found: config_schema.json");
            }
            String content = StreamUtil.readText(inputStream, StandardCharsets.UTF_8);
            String filepath = Paths.get(getContinueGlobalPath(), "config_schema.json").toString();
            new File(filepath).writeText(content);
            return LocalFileSystem.getInstance().findFileByPath(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SchemaType getSchemaType() {
        return SchemaType.embeddedSchema;
    }
}
