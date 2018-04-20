package fr.neutronstars.nbot;

import fr.neutronstars.nbot.entity.Console;
import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.exception.NBotInitializationException;
import fr.neutronstars.nbot.exception.NBotUnsupportedOperationException;
import fr.neutronstars.nbot.plugin.PluginManager;
import fr.neutronstars.nbot.scheduler.Scheduler;
import fr.neutronstars.nbot.util.Configuration;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.impl.NBotLogger;
import org.slf4j.impl.StaticLoggerBinder;

import java.io.IOException;

/**
 * Created by NeutronStars on 30/07/2017
 */
public final class NBot
{
    private static final String NAME = "NBot", VERSION = "2.1.0", AUTHOR = "NeutronStars", JDA_VERSION = "3.6.0_354";
    private static final NBotLogger logger = StaticLoggerBinder.getSingleton().getLoggerFactory().getLogger("NBot");
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

    public static String getJdaVersion()
    {
        return JDA_VERSION;
    }

    public static String getAuthor()
    {
        return AUTHOR;
    }

    public static Guild getGuild(net.dv8tion.jda.core.entities.Guild guild)
    {
        if(guild == null) return null;
        if(server == null) throw new NBotInitializationException("The guild cannot be initialized because the NBotServer hasn't finished loading.");
        return server.getGuild(guild);
    }

    public static void removeGuild(Guild guild)
    {
        server.removeGuild(guild);
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

    public static Scheduler getScheduler()
    {
        return server.getScheduler();
    }

    public static void addJDAListener(EventListener listener)
    {
        if(listener == null) return;
        if(server == null) throw new NBotUnsupportedOperationException("Impossible of register the listener "+listener.getClass().getName()+" because the NBotServer hasn't finished loading.");
        server.getJDA().addEventListener(listener);
    }

    public static void saveLogger()
    {
        try
        {
            StaticLoggerBinder.getSingleton().getLoggerFactory().save();
        }
        catch(IOException e)
        {
            logger.error(e.getMessage(), e);
            logger.info("Stopping NBot in 5 seconds.");
            try
            {
                Thread.sleep(5000L);
            }catch(Exception e1){}
        }
    }
}