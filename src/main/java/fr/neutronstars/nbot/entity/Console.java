package fr.neutronstars.nbot.entity;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.command.CommandManager;
import fr.neutronstars.nbot.plugin.PluginManager;
import org.slf4j.impl.NBotLogger;

import java.util.Scanner;

/**
 * Created by NeutronStars on 19/07/2017
 */
public class Console implements CommandSender, Runnable
{
    private final NBotLogger logger = NBot.getLogger();
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
        CommandManager.onConsoleCommand(this, command);
    }

    public void run()
    {
        while(true)
        {
            if(scanner.hasNextLine())
            {
                performCommand(scanner.nextLine().toLowerCase());
            }
        }
    }

    public void sendMessageToSender(String message)
    {
        logger.console(message);
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
