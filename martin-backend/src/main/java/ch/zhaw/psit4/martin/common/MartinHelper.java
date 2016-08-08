package ch.zhaw.psit4.martin.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import ch.zhaw.psit4.martin.pluginlib.filesystem.FunctionsJSONMissingException;
import edu.stanford.nlp.util.ArraySet;

public class MartinHelper {
    
    
    private static final Log LOG = LogFactory.getLog(MartinHelper.class);

    /**
     * Creates a new ArraySet out of the given set if it is null.
     * If the set is not null it will return it unchanged
     * @param set
     * @return
     */
    public static <T> Set<T> initSetifNull(Set<T> set){
        if(set == null) {
            set = new ArraySet<>();
        }
        return set;
    }

    public static Date parseToSQLDate(String date) throws ParseException {
        if (date != null) {
            java.util.Date parsed = (new SimpleDateFormat("yyyy-MM-dd")).parse(date);
            java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
            return sqlDate;
        } else {
            throw new ParseException("empty Date given",0);
        }
    }
    
    
    /**
     * Get the plugin JSON file for the keywords and functions.
     * 
     * @param keywordsUrl The URL of the file on the filesystem.
     * @return The loaded JSON-file or null if an error occurred.
     */
    public static JSONObject parseJSON(URL url) {
        // keywords JSON loading
        JSONObject json = null;
        try {
            // Get JSON
            InputStream is = url.openStream();
            json = new JSONObject(IOUtils.toString(is));
            is.close();
        } catch (JSONException | IOException e) {
            LOG.error(url.getPath()+ " could not be accessed.", e);
        }

        // check if there is a JSON file
        if (json == null) {
            throw new FunctionsJSONMissingException(
                    url.getPath()+ " missing ");
        }
        return json;
    }

}
