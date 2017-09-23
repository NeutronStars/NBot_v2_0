package fr.neutronstars.nbot.entity;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.NBotStart;
import fr.neutronstars.nbot.logger.NBotLogger;
import fr.neutronstars.nbot.plugin.PluginManager;
import net.dv8tion.jda.core.entities.*;

import java.util.Scanner;

/**
 * Created by NeutronStars on 19/07/2017
 */
public class Console implements CommandSender, Runnable
{
    private final NBotLogger logger = NBotLogger.getLogger("NBot");
    private final Thread thread = new Thread(this, "console");
    private final Scanner scanner = new Scanner(System.in);
    private final PluginManager pluginManager;

    public Console(PluginManager pluginManager){
        this.pluginManager = pluginManager;
        thread.start();
    }

    public String getName()
    {
        return "Server";
    }

    public void performCommand(String command)
    {

    }

    public void run()
    {
        while(true)
        {
            if(scanner.hasNextLine())
            {
                if(scanner.nextLine().equalsIgnoreCase("stop"))
                {
                    break;
                }
            }
        }

        pluginManager.disablePlugins();

        for(net.dv8tion.jda.core.entities.Guild guild : NBot.getJDA().getGuilds())
            NBot.getGuild(guild).save();

        NBot.getJDA().shutdown();
        logger.log(NBot.getName()+ " is down.");

        NBot.saveLogger();
        System.exit(0);
    }

    public void sendMessageToSender(String message)
    {
        logger.log(message);
    }

    public String getAsMention()
    {
        return getName();
    }

    public boolean isConsole()
    {
        return true;
    }
}
