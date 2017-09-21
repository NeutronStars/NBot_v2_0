package fr.neutronstars.nbot;

import fr.neutronstars.nbot.entity.Console;
import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.exception.NBotInitializationException;
import fr.neutronstars.nbot.logger.NBotLogger;
import fr.neutronstars.nbot.plugin.PluginManager;
import fr.neutronstars.nbot.util.Configuration;
import net.dv8tion.jda.core.JDA;

import java.io.IOException;

/**
 * Created by NeutronStars on 30/07/2017
 */
public final class NBot
{
    private static final String NAME = "NBot", VERSION = "2.0.0", AUTHOR = "NeutronStars";
    private static final NBotLogger logger = NBotLogger.getLogger("NBot");
    private static NBotServer server;

    protected static void setServer(NBotServer server)
    {
        if(NBot.server != null) throw new NBotInitializationException("The class NBotServer is already initialized.");
        NBot.server = server;
    }

    public static JDA getJDA()
    {
        return server.getJDA();
    }

    public static NBotLogger getLogger()
    {
        return logger;
    }

    public static String getName()
    {
        return NAME;
    }

    public static String getVersion()
    {
        return VERSION;
    }

    public static String getAuthor()
    {
        return AUTHOR;
    }

    public static Guild getGuild(net.dv8tion.jda.core.entities.Guild guild)
    {
        return server.getGuild(guild);
    }

    public static Console getConsole()
    {
        return server.getConsole();
    }

    public static PluginManager getPluginManager()
    {
        return server.getPluginManager();
    }

    public static Configuration getConfiguration()
    {
        return server.getNBotConfiguration();
    }

    public static void saveLogger()
    {
        try
        {
            NBotLogger.save("logs", logger.nowTimeFormat().split("-")[0].replace("/", "-"));
        }
        catch(IOException e)
        {
            logger.logThrowable(e);
            logger.log("Stopping NBot in 5 seconds.");
            try
            {
                Thread.sleep(5000L);
            }catch(Exception e1){}
        }
    }
}
