package fr.neutronstars.nbot.command.defaut;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.command.Command;
import fr.neutronstars.nbot.command.SimpleCommand;
import fr.neutronstars.nbot.entity.Channel;
import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.entity.Message;
import fr.neutronstars.nbot.entity.User;
import fr.neutronstars.nbot.plugin.NBotPlugin;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;

import java.awt.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by NeutronStars on 21/09/2017
 */
public class DefaultCommand
{
    @Command(name = "help")
    private void onHelp(User user, Channel channel, Guild guild)
    {
        Collection<SimpleCommand> commands = guild.getCommands();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Commands List");
        builder.setDescription("Commands for the guild "+guild.getName()+"\n  -> Prefix : "+guild.getPrefix());
        builder.setColor(Color.MAGENTA);
        builder.setFooter("API "+NBot.getName()+" v"+NBot.getVersion()+" by "+NBot.getAuthor(), null);

        for(SimpleCommand command : commands)
        {
            if(!guild.hasPermission(user, command.getPower())) continue;
            String value = "[>](1) Description : "+command.getDescription();
            if(command.getAliases().size() > 0) value += "\n[>](2) Aliases : "+command.getAliasesToString();
            builder.addField(command.getName(), value, false);
        }

        user.sendMessageToChannel(builder.build());
        channel.sendMessageToChannel(user.getAsMention()+" check your private message !");
    }

    @Command(name="plugins")
    private void onPlugins(Channel channel)
    {
        Collection<NBotPlugin> plugins = NBot.getPluginManager().getPlugins();
        if(plugins.size() == 0)
        {
            channel.sendMessageToChannel("There is no plugin.");
            return;
        }
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Plugins list");
        builder.setColor(Color.RED);
        builder.setFooter("API "+NBot.getName()+" v"+NBot.getVersion()+" by "+NBot.getAuthor(), null);

        for(NBotPlugin plugin : plugins)
            builder.addField(plugin.getName(), "[>](1) Version : "+plugin.getVersion()+"\n[>](2) Author(s) : "+plugin.getAuthorsToString(), true);

        channel.sendMessageToChannel(builder.build());
    }

    @Command(name = "prefix", powers = 100)
    private void onPrefix(Guild guild, Channel channel, String[] args)
    {
        String prefix = args.length == 0 || args[0].equalsIgnoreCase("null") ? null : args[0];
        guild.setPrefix(prefix);
        channel.sendMessageToChannel("Prefix modified -> "+prefix);
    }

    @Command(name="power", powers = 100)
    private void onPower(SimpleCommand command, Message message, String[] args, Guild guild)
    {
        if(args.length < 2)
        {
            message.getMessageChannel().sendMessageToChannel(command.getSimpleName()+" <power> <User|Role>");
            return;
        }

        int powers = 0;
        try {
            powers = Integer.parseInt(args[0]);
        }catch(NumberFormatException nfe){
            message.getMessageChannel().sendMessageToChannel(command.getSimpleName()+" <power> <User|Role>");
            return;
        }

        if(message.getMentionedUsers().size() == 0 && message.getMentionedRoles().size() == 0)
        {
            SimpleCommand simpleCommand = guild.getCommand(args[1]);
            if(simpleCommand == null) message.getMessageChannel().sendMessageToChannel(command.getSimpleName()+" <power> <User|Role>");
            else
            {
                simpleCommand.setPower(powers);
                message.getMessageChannel().sendMessageToChannel("Power modified for the command "+args[2]);
            }
            return;
        }

        StringBuilder builder = new StringBuilder("Permission added ->");

        for(net.dv8tion.jda.core.entities.User user : message.getMentionedUsers())
        {
            guild.setPermission(user, powers);
            builder.append(" ").append(user.getAsMention());
        }

        for(Role role : message.getMentionedRoles())
        {
            guild.setPermission(role, powers);
            builder.append(" ").append(role.getAsMention());
        }

        message.getMessageChannel().sendMessageToChannel(builder.toString());
    }
}
