package fr.neutronstars.nbot;

import fr.neutronstars.nbot.command.CommandManager;
import fr.neutronstars.nbot.command.defaut.DefaultCommand;
import fr.neutronstars.nbot.exception.NBotConfigurationException;
import fr.neutronstars.nbot.logger.NBotLogger;
import fr.neutronstars.nbot.plugin.PluginManager;
import fr.neutronstars.nbot.util.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NeutronStars on 30/07/2017
 */
public class NBotStart
{
    private static final NBotLogger logger = NBotLogger.getLogger("NBot");

    public static void main(String... args)
    {
        logger.log(String.format("Starting %1$s v%2$s by %3$s...", NBot.getName(), NBot.getVersion(), NBot.getAuthor()));

        loadFolders("guilds", "plugins", "config");

        Configuration configuration = loadConfiguration();
        setDefaultConfiguration(configuration);

        PluginManager pluginManager = new PluginManager(configuration.getString("loadedFormat"), configuration.getString("enabledFormat"), configuration.getString("disabledFormat"));
        CommandManager.registerCommand(new DefaultCommand(), null);
        pluginManager.registerCommands();

        try
        {
            NBot.setServer(new NBotServer(configuration, pluginManager));
        } catch(Exception e)
        {
            logger.logThrowable(e);
            NBot.saveLogger();
        }
    }

    private static Configuration loadConfiguration()
    {
        return  Configuration.loadConfiguration(new File("config/config.json"));
    }

    private static void setDefaultConfiguration(Configuration configuration)
    {
        if(configuration == null)
        {
            logger.logThrowable(new NBotConfigurationException("The config cannot be null."));
            NBot.saveLogger();
            System.exit(0);
        }

        if(!configuration.has("token")) configuration.set("token", "Insert your token here.");

        if(!configuration.has("dateFormat")) configuration.set("dateFormat", NBotLogger.getDateFormat());
        NBotLogger.setDateFormat(configuration.getString("dateFormat"));

        if(!configuration.has("pattern")) configuration.set("pattern", NBotLogger.getPattern());
        NBotLogger.setPattern(configuration.getString("pattern"));

        if(!configuration.has("playing")) configuration.set("playing", "null");

        if(!configuration.has("loadedFormat")) configuration.set("loadedFormat", "%1$s v%2$s by %3$s is loaded.");
        if(!configuration.has("enabledFormat")) configuration.set("enabledFormat", "%1$s v%2$s by %3$s is enabled.");
        if(!configuration.has("disabledFormat")) configuration.set("disabledFormat", "%1$s v%2$s by %3$s is disabled.");

        configuration.save();
    }

    private static void loadFolders(String... names)
    {
        for(String name: names)
        {
            File file = new File(name);
            if(!file.exists()) file.mkdir();
        }
    }
}
