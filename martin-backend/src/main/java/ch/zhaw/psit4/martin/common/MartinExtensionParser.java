package ch.zhaw.psit4.martin.common;

import java.sql.Date;
import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.registry.Extension;

import ch.zhaw.psit4.martin.models.MAuthor;
import ch.zhaw.psit4.martin.models.MPlugin;

public class MartinExtensionParser {
    
    
    private static final Log LOG = LogFactory.getLog(MartinExtensionParser.class);

    /** Get the author Metadata from plugins.xml
     * 
     * @param extension The extension to get the data from.
     */
	public static MAuthor getAuthorFromExtension(Extension pluginFrameWorkData){
	    MAuthor author = new MAuthor();
        // update DB-object
        author.setName(pluginFrameWorkData.getParameter("author").valueAsString());
        author.setEmail(pluginFrameWorkData.getParameter("e-mail").valueAsString());
        return author;
	}

    public static MPlugin getPluginFroExtension(Extension pluginFrameworkData) {
        org.java.plugin.registry.Extension.Parameter pluginName = pluginFrameworkData.getParameter("name");

        org.java.plugin.registry.Extension.Parameter pluginDesctibtion = pluginFrameworkData.getParameter("description");
        org.java.plugin.registry.Extension.Parameter pluginDate = pluginFrameworkData.getParameter("date");
        String uuid = pluginFrameworkData.getId();
       
        String description = (pluginDesctibtion != null)?pluginDesctibtion.valueAsString():"No description provided.";
        Date date = null;
        try {
        date = MartinHelper.parseToSQLDate(pluginDate.valueAsString());
        } catch (ParseException e) {
            LOG.error("Could not parse SQL Date in Plugin");
        }
        MPlugin plugin = new MPlugin(uuid,
                                    pluginName.valueAsString(),
                                    description,
                                    date);
        return plugin;

    }

}
