package fr.neutronstars.nbot.command.defaut;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.command.Command;
import fr.neutronstars.nbot.entity.CommandSender;

public class ConsoleCommand
{
    @Command(name = "stop", description = "Stop NBot API", executor = Command.ExecutorType.CONSOLE)
    private void onStop(CommandSender sender)
    {
        sender.sendMessageToSender("Stopping bot...");

        NBot.getPluginManager().disablePlugins();

        for(net.dv8tion.jda.core.entities.Guild guild : NBot.getJDA().getGuilds())
            NBot.getGuild(guild).save();

        NBot.getJDA().shutdown();
        NBot.getLogger().info(NBot.getName()+ " is down.");

        NBot.saveLogger();
        System.exit(0);
    }
}
