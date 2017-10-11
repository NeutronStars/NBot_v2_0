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
import net.dv8tion.jda.core.entities.SelfUser;

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
            builder.addField(command.getSimpleName(), value, false);
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
    private void onPower(SimpleCommand command, User user1, Message message, String[] args, Guild guild)
    {
        if(args.length < 2)
        {
            message.getMessageChannel().sendMessageToChannel(command.getSimpleName()+" <power> <User|Role|Command>");
            return;
        }

        int powers = 0;
        try {
            powers = Integer.parseInt(args[0]);
        }catch(NumberFormatException nfe){
            message.getMessageChannel().sendMessageToChannel(command.getSimpleName()+" <power> <User|Role|Command>");
            return;
        }

        if(message.getMentionedUsers().size() == 0 && message.getMentionedRoles().size() == 0)
        {
            SimpleCommand simpleCommand = guild.getCommand(args[1]);
            if(simpleCommand == null)
                message.getMessageChannel().sendMessageToChannel(command.getSimpleName() + " <power> <User|Role|Command>");
            else
            {
                if(!guild.hasPermission(user1, simpleCommand.getPower()))
                    return;
                simpleCommand.setPower(powers);
                message.getMessageChannel().sendMessageToChannel("Power modified for the command " + args[1]);
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

    @Command(name = "commandname", powers = 100)
    private void onCommandName(SimpleCommand simpleCommand, User user, String[] args, Guild guild, Channel channel)
    {
        if(args.length < 2)
        {
            channel.sendMessageToChannel(simpleCommand.getSimpleName()+" <commandName> <newName>");
            return;
        }
        SimpleCommand command = guild.getCommand(args[0]);

        if(command == null)
        {
            channel.sendMessageToChannel(simpleCommand.getSimpleName()+" <commandName> <newName>");
            return;
        }

        if(!guild.hasPermission(user, command.getPower())) return;

        if(args[1].equalsIgnoreCase("") || args[1].equalsIgnoreCase("null")
                || args[1].equalsIgnoreCase("reset") || args[1].equalsIgnoreCase("-r"))
        {
            guild.setCustomNameCommand(command.getSimpleName(), null);
            channel.sendMessageToChannel("Command name reset for "+command.getName());
            return;
        }

        if(guild.setCustomNameCommand(command.getSimpleName(), args[1]))
        {
            channel.sendMessageToChannel("Command name changed for "+args[0]+" >> "+command.getSimpleName());
            return;
        }

        channel.sendMessageToChannel("Impossible assignied the name "+args[1]+" for the command "+command.getSimpleName());
    }

    @Command(name = "info",description = "Show the infos of this bot.")
    private void info(SelfUser user, Channel channel)
    {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(user.getName(), "https://discordapp.com/oauth2/authorize?client_id="+user.getId()+"&scope=bot&permissions=2146958583", user.getAvatarUrl()+"?size=256");
        builder.setDescription("To invite the bot to your guild, click on its name above.");
        builder.addBlankField(false);
        builder.addField("Guilds", "[>](1) "+NBot.getJDA().getGuilds().size(), true);
        builder.addField("Plugins", "[>](1) "+NBot.getPluginManager().getPlugins().size(), true);
        builder.setFooter(NBot.getName()+" v"+NBot.getVersion()+" by "+NBot.getAuthor(), "");
        builder.setColor(Color.RED);
        channel.sendMessageToChannel(builder.build());
    }
}
