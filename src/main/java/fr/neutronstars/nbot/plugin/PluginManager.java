package fr.neutronstars.nbot.plugin;

import fr.neutronstars.nbot.exception.NBotPluginException;
import fr.neutronstars.nbot.logger.NBotLogger;
import fr.neutronstars.nbot.util.JSONReader;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * Created by NeutronStars on 01/08/2017
 */
public final class PluginManager
{
    private final Map<Class<? extends NBotPlugin>, NBotPlugin> plugins = new HashMap<>();
    private final NBotLogger logger = NBotLogger.getLogger("NBot");
    private final String loadedFormat, enabledFormat, disabledFormat;

    public PluginManager(String loadedFormat, String enabledFormat, String disabledFormat)
    {
        this.loadedFormat = loadedFormat;
        this.enabledFormat = enabledFormat;
        this.disabledFormat = disabledFormat;
        for(File file : new File("plugins").listFiles()) loadPlugin(file);
    }

    public <T extends NBotPlugin> T getPlugin(Class<T> clazz)
    {
        return (T) plugins.get(clazz);
    }

    public Collection<NBotPlugin> getPlugins()
    {
        return new ArrayList<>(plugins.values());
    }

    private void loadPlugin(File file)
    {
        if(file == null || file.isDirectory() || !file.getName().endsWith(".jar")) return;
        NBotPlugin nBotPlugin = loadNBotPlugin(file);
        if(nBotPlugin == null) return;
        nBotPlugin.onLoad();
        logger.log(String.format(loadedFormat, nBotPlugin.getName(), nBotPlugin.getVersion(), nBotPlugin.getAuthorsToString()));
    }

    private NBotPlugin loadNBotPlugin(File file)
    {
        try(JarFile jar = new JarFile(file))
        {
            JSONReader reader = new JSONReader(new BufferedReader(new InputStreamReader(jar.getInputStream(jar.getJarEntry("plugin.json")))));
            JSONObject object = reader.toJSONObject();

            if(!object.has("main") || !object.has("name") || !object.has("version")) throw new NBotPluginException("Invalid \"plugin.json\".");
            NBotPlugin nBotPlugin = new NBotClassLoader(object.getString("main"), getClass().getClassLoader(), file).getPlugin();

            nBotPlugin.setName(object.getString("name"));
            nBotPlugin.setVersion(object.getString("version"));

            plugins.put(nBotPlugin.getClass(), nBotPlugin);
            return nBotPlugin;
        }
        catch(Exception e)
        {
            logger.logThrowable(new NBotPluginException(e));
        }
        return null;
    }

    public void registerCommands()
    {
        for(NBotPlugin plugin : plugins.values())
            plugin.onRegisterCommands();
    }

    public void enablePlugins()
    {
        for(NBotPlugin nBotPlugin : plugins.values())
        {
            nBotPlugin.onEnable();
            logger.log(String.format(enabledFormat, nBotPlugin.getName(), nBotPlugin.getVersion(), nBotPlugin.getAuthorsToString()));
        }
    }

    public void disablePlugins()
    {
        for(NBotPlugin nBotPlugin : plugins.values())
        {
            nBotPlugin.onDisable();
            logger.log(String.format(disabledFormat, nBotPlugin.getName(), nBotPlugin.getVersion(), nBotPlugin.getAuthorsToString()));
            try {
                nBotPlugin.getClassLoader().close();
            } catch(IOException e) {
                logger.logThrowable(e);
            }
        }
    }
}
