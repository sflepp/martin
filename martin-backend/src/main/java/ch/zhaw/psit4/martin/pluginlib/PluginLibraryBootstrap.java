/**
 * 
 */
package ch.zhaw.psit4.martin.pluginlib;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.ObjectFactory;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.PluginLocation;
import org.java.plugin.boot.DefaultPluginsCollector;
import org.java.plugin.util.ExtendedProperties;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.api.MartinAPIDefines;
import ch.zhaw.psit4.martin.pluginlib.filesystem.PluginFolderAccessor;

/**
 * Entry point for the module library to bootstrap jpf and invoke <code>PluginLibrary.start()</code>
 * .
 *
 * @version 0.0.1-SNAPSHOT
 */
public class PluginLibraryBootstrap {
    /**
     * Boots up the module library
     */
    private static final Log LOG = LogFactory.getLog(PluginLibraryBootstrap.class);
    /**
     * The plugin Manager from org.java.plugin
     */
    private final PluginManager manager;
    /**
     * The plugin Collector from org.java.plugin.boot
     */
    private final DefaultPluginsCollector collector;
    /**
     * The ExtendedProperties object from org.java.plugin.util
     */
    private final ExtendedProperties props;

    @Autowired
    private PluginFolderAccessor folderAccessor;

    public PluginLibraryBootstrap() {
        // instantiate necessary objects
        manager = ObjectFactory.newInstance().createManager();
        collector = new DefaultPluginsCollector();
        props = new ExtendedProperties();
    }

    /**
     * Boots the plugin library by calling the plugin api and returns it once booted.
     * 
     * @return The booted plugin Library
     */
    public IPluginLibrary boot() {
        // get the plugin folder
        File file = folderAccessor.getPluginFolder();
        if (file == null) {
            LOG.error("Plugin library could not be initialized!");
            throw new PluginLibraryNotFoundException();
        }

        // prepare configuration
        props.setProperty("org.java.plugin.boot.pluginsRepositories", file.getPath());

        // try to initialize the library
        IPluginLibrary lib;
        try {
            lib = initializeLibrary();
            lib.initialize(collector);
        } catch (Exception e) {
            LOG.error("Plugin library could not be initialized!", e);
            throw new PluginLibraryNotFoundException(e);
        }

        // finally return library
        return lib;
    }

    /**
     * Initializes the library by configuring the Plugincollector and querrying the PluginManager
     * 
     * @return The plugin library interface
     * @throws Exception
     */
    private IPluginLibrary initializeLibrary() throws Exception {
        collector.configure(props);
        // examine plugins repository for plugins
        manager.publishPlugins(collector.collectPluginLocations().toArray(new PluginLocation[] {}));

        // finally retrieve the core plugin and start it up
        return (IPluginLibrary) manager.getPlugin(MartinAPIDefines.CORE_PLUGIN_ID.getValue());
    }
}
