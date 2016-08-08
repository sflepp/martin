package ch.zhaw.psit4.martin.pluginlib.filesystem;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class PluginFolderAccessorTest {
    
    @Autowired
    PluginFolderAccessor validFolderAccessor;
    @Autowired
    PluginFolderAccessor noJSONFolderAccessor;
    @Autowired
    PluginFolderAccessor noFolderAccessor;
    @Autowired
    PluginFolderAccessor faultyFolderAccessor;

    @Before
    public void setUp() {
        String pluginsFolder = "plugins";
        String pluginsJSON = "library-test.cfg.json";
        String missingFolder = "missingFolder";
        String missingJSON = "missing-lib.cfg.json";
        
        validFolderAccessor.setFolderName(pluginsFolder);
        validFolderAccessor.setConfigFile(pluginsJSON);

        noJSONFolderAccessor.setFolderName(pluginsFolder);
        noJSONFolderAccessor.setConfigFile(missingJSON);
        
        noFolderAccessor.setFolderName(missingFolder);
        noFolderAccessor.setConfigFile(pluginsJSON);
        
        faultyFolderAccessor.setFolderName(missingFolder);
        faultyFolderAccessor.setConfigFile(missingJSON);
    }

    public void testGetPluginFolder() {
        File valid = validFolderAccessor.getPluginFolder();
        File noJSON = noJSONFolderAccessor.getPluginFolder();
        File noFolder = noFolderAccessor.getPluginFolder();
        File faulty = faultyFolderAccessor.getPluginFolder();
        
        assertNotNull(valid);
        assertNotNull(noJSON);
        assertNull(noFolder);
        assertNull(faulty);
    }

}
