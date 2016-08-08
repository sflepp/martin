package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * This class handles searching for plugins in the file-system of the backend server.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class PluginFolderAccessor implements ResourceLoaderAware {
    private ResourceLoader resourceLoader;
    /**
     * Path to folder where plugins reside (either zipped, or unpacked as a simple folder)
     */
    private String folderName;
    /**
     * The plugin configuration file
     */
    private String configFile;
    /*
     * The found plugin folder
     */
    private File foundFolder;

    private static final Log LOG = LogFactory.getLog(PluginFolderAccessor.class);

    public PluginFolderAccessor(String folderName, String configFile) {
        this.folderName = folderName;
        this.configFile = configFile;
        this.foundFolder = null;
    }

    /**
     * Resets the found plugin folder to null allowing the class to search again.
     */
    public void resetFoundFolder() {
        try {
            LOG.info("Reseting found folder: " + foundFolder.getCanonicalPath() + " to null.");
        } catch (IOException e) {
            LOG.info("No folder was found yet.", e);
        }
        this.foundFolder = null;
    }

    /**
     * Gathers paths from config and finds the plugin folder.
     * 
     * @return The plugin folder.
     */
    public File getPluginFolder() {
        if (foundFolder != null)
            return foundFolder;

        // load library config json
        JSONObject libConfig = null;
        try {
            Resource resource = resourceLoader.getResource(configFile);
            InputStream resourceInputStream = resource.getInputStream();
            libConfig = new JSONObject(IOUtils.toString(resourceInputStream));
            resourceInputStream.close();
        } catch (IOException e) {
            LOG.warn("Missing " + configFile + "!", e);
        }

        // search the json registered paths
        if (libConfig != null) {
            foundFolder = getFolderFromPaths(libConfig.getJSONArray("paths"));
        } else {
            LOG.error(configFile + " can't be loaded!");
        }

        return foundFolder;
    }

    /**
     * Get the plugin folder from an array of paths.
     * 
     * @param paths The paths in a {@link JSONArray} file.
     * @return The found file.
     */
    File getFolderFromPaths(JSONArray paths) {
        File out = null;
        for (int i = 0; i < paths.length(); i++) {
            try {
                File test = new File(paths.get(i).toString());
                LOG.info("Checking: " + test.getCanonicalPath() + ".");
                out = checkFolder(test.getCanonicalPath(), folderName);
                // if a folder was found, return
                if (out != null)
                    return out;
            } catch (IOException e) {
                LOG.warn("Path " + paths.get(i).toString() + " could not be accessed, skipped.", e);
                continue;
            }
        }
        return out;
    }

    /**
     * Check if the folder is the searched folder.
     * 
     * @param source The source path to search.
     * @param folder The folder name to search.
     * @return The found folder or null if no folder was found.
     * @throws IOException
     */
    File checkFolder(String source, String folder) throws IOException {
        File out = null;
        String[] sourceParts = source.split("/|\\\\");
        if (sourceParts[sourceParts.length - 1].equals(folder)) {
            out = new FileSystemResource(source).getFile();
            if (out.exists() && out.isDirectory()) {
                LOG.info("Source: " + source + " is a plugin folder.");
                return out;
            } else {
                LOG.warn("Path " + out.getCanonicalPath() + " is not a valid plugin folder.");
                return null;
            }
        }
        return out;
    }

    public String getFolderPath() {
        String path = null;
        if (foundFolder != null)
            try {
                path = foundFolder.getCanonicalPath();
            } catch (IOException e) {
                LOG.warn("Could not retireve cononical path.", e);
                path = foundFolder.getAbsolutePath();
            }
        return path;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource getResource(String location) {
        return resourceLoader.getResource(location);
    }
}
