package fr.neutronstars.nbot;

import fr.neutronstars.nbot.command.CommandManager;
import fr.neutronstars.nbot.command.defaut.DefaultCommand;
import fr.neutronstars.nbot.exception.NBotConfigurationException;
import fr.neutronstars.nbot.plugin.PluginManager;
import fr.neutronstars.nbot.util.Configuration;
import org.slf4j.impl.NBotLogger;
import org.slf4j.impl.StaticLoggerBinder;

import java.io.File;
/**
 * Created by NeutronStars on 30/07/2017
 */
public class NBotStart
{
    private static final NBotLogger logger = StaticLoggerBinder.getSingleton().getLoggerFactory().getLogger("NBot");

    public static void main(String... args)
    {
        System.setProperty("file.encoding", "UTF-8");

        logger.info(String.format("Starting %1$s v%2$s by %3$s...", NBot.getName(), NBot.getVersion(), NBot.getAuthor()));

        loadFolders("guilds", "plugins", "config");

        Configuration configuration = loadConfiguration();
        setDefaultConfiguration(configuration);

        PluginManager pluginManager = new PluginManager(configuration.getString("loadedFormat"), configuration.getString("enabledFormat"), configuration.getString("disabledFormat"));
        CommandManager.registerCommand(new DefaultCommand(), null);
        pluginManager.registerCommands();

        try
        {
            NBotServer server = new NBotServer(configuration, pluginManager);
            NBot.setServer(server);
            loop(server);
        } catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            NBot.saveLogger();
        }
    }

    private static void loop(NBotServer server)
    {
        long lns = System.nanoTime();
        double ns = 1000000000.0/20.0;
        long ls = System.currentTimeMillis();

        int tps = 0;

        while(true)
        {
            if(System.nanoTime() - lns > ns)
            {
                lns += ns;
                update();
                tps++;
            }

            if(System.currentTimeMillis() - ls >= 1000)
            {
                ls = System.currentTimeMillis();
                server.setTps(tps);
                tps = 0;
            }
        }
    }

    private static void update()
    {
        NBot.getScheduler().updateTasks();
    }

    private static Configuration loadConfiguration()
    {
        return  Configuration.loadConfiguration(new File("config/config.json"));
    }

    private static void setDefaultConfiguration(Configuration configuration)
    {
        if(configuration == null)
        {
            logger.error("The config cannot be null.", new NBotConfigurationException("The config cannot be null."));
            NBot.saveLogger();
            System.exit(0);
        }

        if(!configuration.has("token")) configuration.set("token", "Insert your token here.");

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
