package ch.zhaw.psit4.martin.pluginlib.filesystem;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.registry.Extension;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.common.MartinExtensionParser;
import ch.zhaw.psit4.martin.common.MartinHelper;
import ch.zhaw.psit4.martin.models.MExampleCall;
import ch.zhaw.psit4.martin.models.MFunction;
import ch.zhaw.psit4.martin.models.MKeyword;
import ch.zhaw.psit4.martin.models.MParameter;
import ch.zhaw.psit4.martin.models.MPlugin;
import ch.zhaw.psit4.martin.models.repositories.MKeywordRepository;
import ch.zhaw.psit4.martin.models.repositories.MPluginRepository;

public class PluginDataAccessor {

    /**
     * File name of the plugin keywords JSON.
     */
    public static final String PLUGIN_FUNCTIONS = "functions.json";

    private static final Log LOG = LogFactory.getLog(PluginDataAccessor.class);

    @Autowired
    private MPluginRepository pluginRepository;

    @Autowired
    private MKeywordRepository keywordRepository;

    public void savePluginInDB(Extension pluginData, ClassLoader classLoader)
            throws FunctionsJSONMissingException {

        MPlugin dbPlugin = createPluginFromFrameworkData(pluginData);

        if (pluginRepository.findByUuid(dbPlugin.getUuid()) == null) {
            // Plugin is new
            JSONObject jsonPluginSource =
                    MartinHelper.parseJSON(classLoader.getResource(PLUGIN_FUNCTIONS));
            addJSONDataToPlugin(jsonPluginSource, dbPlugin);
            pluginRepository.save(dbPlugin);
        } else {
            LOG.info("Plugin " + dbPlugin.getName() + " already exists in DB");
        }
    }

    private MPlugin createPluginFromFrameworkData(Extension extension) {

        MPlugin plugin = MartinExtensionParser.getPluginFroExtension(extension);

        if (plugin.getUuid() == null) {
            LOG.error("Extension ID not accessible, generating new UUID");
            plugin.setUuid(UUID.randomUUID().toString());
        }

        plugin.setAuthor(MartinExtensionParser.getAuthorFromExtension(extension));

        return plugin;
    }

    private void addJSONDataToPlugin(JSONObject jsonPluginSource, MPlugin dbPlugin) {
        Set<MFunction> functionsFromJson = parsePluginFunctions(jsonPluginSource, dbPlugin);
        functionsFromJson.stream().forEach(f -> dbPlugin.addFunction(f));
    }

    private Set<MFunction> parsePluginFunctions(JSONObject json, MPlugin plugin) {

        ArrayList<MFunction> functions = new ArrayList<>();
        JSONArray jsonFunctions = json.getJSONArray("Functions");
        for (int functionNumber = 0; functionNumber < jsonFunctions.length(); functionNumber++) {
            JSONObject jsonFunction = jsonFunctions.getJSONObject(functionNumber);
            functions.add(assambleFunctionFromJSON(jsonFunction));
        }
        return new HashSet<>(functions);
    }

    private MFunction assambleFunctionFromJSON(JSONObject jsonFunction) {
        MFunction function = new MFunction(jsonFunction.getString("Name"),
                jsonFunction.getString("Description"));

        Set<MParameter> functionParameter = parseFunctionParameters(jsonFunction, function);
        functionParameter.stream().forEach(p -> function.addParameter(p));

        addKeywordsToFunction(jsonFunction, function);
        addExampleCallsToFunction(jsonFunction, function);
        return function;
    }

    private Set<MParameter> parseFunctionParameters(JSONObject jsonFunction, MFunction function) {
        List<MParameter> functionParameter = new ArrayList<>();

        JSONArray jsonParameter = jsonFunction.getJSONArray("Parameter");

        for (int i = 0; i < jsonParameter.length(); i++) {
            JSONObject jsonparam = jsonParameter.getJSONObject(i);
            functionParameter.add(assambleParameterFromJSON(jsonparam));
        }

        return new HashSet<>(functionParameter);
    }

    private MParameter assambleParameterFromJSON(JSONObject jsonparam) {
        MParameter parameter = new MParameter(jsonparam.getString("Name"),
                    jsonparam.getBoolean("Required"),
                    jsonparam.getString("Type"));
        addKeywordsToParameter(jsonparam, parameter);
        return parameter;
    }

    private void addKeywordsToParameter(JSONObject jsonParameter, MParameter param) {
        if (jsonParameter.getJSONArray("Keywords") != null
                && jsonParameter.getJSONArray("Keywords").length() >= 1) {

            JSONArray jsonKeywords = jsonParameter.getJSONArray("Keywords");
            for (int keyWordNum = 0; keyWordNum < jsonKeywords.length(); keyWordNum++) {
                String keywordName = jsonKeywords.getString(keyWordNum);
                MKeyword keyword = keywordRepository.findByKeywordIgnoreCase(keywordName);
                if (keyword == null) {
                    keyword = new MKeyword();
                    keyword.setKeyword(keywordName);
                }
                param.addKeyword(keyword);
            }
        }
    }

    private void addExampleCallsToFunction(JSONObject jsonFunction, MFunction function) {
        JSONArray jsonCalls = jsonFunction.getJSONArray("Examples");
        for (int callNumber = 0; callNumber < jsonCalls.length(); callNumber++) {
            JSONObject jsonCall = jsonCalls.getJSONObject(callNumber);
            MExampleCall exampleCall = new MExampleCall(jsonCall.getString("Call"),
                    jsonCall.getString("Description"));
            function.addExampleCall(exampleCall);
        }
    }


    private void addKeywordsToFunction(JSONObject jsonFunction, MFunction function) {
        JSONArray jsonKeywords = jsonFunction.getJSONArray("Keywords");
        for (int keyWordNum = 0; keyWordNum < jsonKeywords.length(); keyWordNum++) {
            MKeyword keyword = new MKeyword();
            keyword.setKeyword(jsonKeywords.getString(keyWordNum));

            function.addKeyword(keyword);
        }
    }

}
