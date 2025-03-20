package com.github.continuedev.continueeclipseextension;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.source.ISchemaProvider;
import org.eclipse.jface.text.source.IURLInputStreamProvider;
import org.eclipse.wst.json.core.schema.ISchemaFileProvider;
import org.eclipse.wst.json.core.schema.ISchemaProviderFactory;
import org.eclipse.wst.json.core.schema.SchemaType;
import com.github.continuedev.continueeclipseextension.activities.ContinuePluginStartupActivity;
import com.github.continuedev.continueeclipseextension.constants.ContinueConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class ConfigRcJsonSchemaProviderFactory implements ISchemaProviderFactory {

    @Override
    public ISchemaProvider createSchemaProvider() {
        return new ConfigRcJsonSchemaFileProvider();
    }
}

class ConfigRcJsonSchemaFileProvider implements ISchemaFileProvider, IURLInputStreamProvider {

    @Override
    public boolean isApplicable(String fileName) {
        return fileName.equals(".continuerc.json");
    }

    @Override
    public String getDisplayName() {
        return ".continuerc.json";
    }

    @Override
    public String getSchemaURI() {
        return getSchemaFile().getFullPath().toString();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = ContinuePluginStartupActivity.class.getClassLoader().getResourceAsStream("continue_rc_schema.json");
        if (inputStream == null) {
            throw new IOException("Resource not found: continue_rc_schema.json");
        }
        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        String filePath = Paths.get(ContinueConstants.getContinueGlobalPath(), "continue_rc_schema.json").toString();
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
        return file.toURI().toURL().openStream();
    }

    @Override
    public SchemaType getSchemaType() {
        return SchemaType.embedded;
    }

    @Override
    public IFile getSchemaFile() {
        String filePath = Paths.get(ContinueConstants.getContinueGlobalPath(), "continue_rc_schema.json").toString();
        File file = new File(filePath);
        return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(file.getAbsolutePath()));
    }
}