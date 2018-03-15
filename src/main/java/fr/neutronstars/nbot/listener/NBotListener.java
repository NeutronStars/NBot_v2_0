package fr.neutronstars.nbot.listener;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.entity.Message;
import fr.neutronstars.nbot.entity.User;
import fr.neutronstars.nbot.plugin.PluginManager;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.impl.NBotLogger;

import java.io.File;

/**
 * Created by NeutronStars on 01/08/2017
 */
public class NBotListener extends ListenerAdapter
{
    private final NBotLogger logger = NBot.getLogger();
    private final PluginManager pluginManager;

    public NBotListener(PluginManager pluginManager)
    {
        this.pluginManager = pluginManager;
    }

    public void onReady(ReadyEvent event)
    {
        pluginManager.enablePlugins();

        StringBuilder builder = new StringBuilder("\n-------------------------------\n")
                .append(event.getJDA().getSelfUser().getName()).append(" is Ready.\n-------------------------------");

        for(net.dv8tion.jda.core.entities.Guild guild : event.getJDA().getGuilds())
        {
            Guild guild1 = NBot.getGuild(guild);
            builder.append("\n  -> ").append(guild1.getName());
        }
        builder.append("\n-------------------------------");

        String playing = NBot.getConfiguration().getString("playing");
        if(playing != null && !playing.equalsIgnoreCase("null"))
            NBot.getJDA().getPresence().setGame(Game.listening(playing));

        logger.info(builder.toString());

        NBot.getConsole();

        File folder = new File("guilds");

        for(File file : folder.listFiles())
        {
            if(!file.isDirectory()) continue;
            if(event.getJDA().getGuildById(file.getName()) == null) deleteFile(file);
        }
    }

    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot() || event.getTextChannel() == null) return;
        Guild guild = NBot.getGuild(event.getGuild());
        String message = event.getMessage().getContentRaw();
        if(!message.startsWith(guild.getPrefix())) return;

        message = message.replaceFirst(guild.getPrefix(), "");
        Message message1 = new Message(event.getMessage());

        if(guild.executeCommand(new User(event.getAuthor()), message, message1))
            message1.deleteTheMessage();
    }

    private void deleteFile(File file)
    {
        if(file == null) return;
        if(file.isDirectory())
        {
            for(File file1 : file.listFiles())
                deleteFile(file1);
        }
        file.delete();
    }
}
