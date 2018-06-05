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
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by NeutronStars on 21/09/2017
 */
public class DefaultCommand
{
    @Command(name="plugins", description = "Show the list of plugins.")
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

    @Command(name = "prefix", powers = 100, description = "Change the prefix for the commands of the guild.")
    private void onPrefix(Guild guild, Channel channel, String[] args)
    {
        String prefix = args.length == 0 || args[0].equalsIgnoreCase("null") ? null : args[0];
        guild.setPrefix(prefix);
        channel.sendMessageToChannel("Prefix modified -> "+prefix);
    }

    @Command(name="power", powers = 100, description = "Change the permissions of commands, users or role of the guild.")
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

    @Command(name = "commandname", powers = 100, description = "change the commands name of the guild.")
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
        builder.setAuthor(user.getName(), "https://discordapp.com/oauth2/authorize?client_id=" + user.getId() + "&scope=bot&permissions=2146958583", user.getAvatarUrl() == null ? null : user.getAvatarUrl() + "?size=256");
        builder.setDescription("To invite the bot to your guild, click on its name above.");
        builder.addBlankField(false);
        builder.addField("Guilds", "[>](1) " + NBot.getJDA().getGuilds().size(), true);
        builder.addField("Users", "[>](1) " + NBot.getJDA().getUsers().size(), true);
        builder.addBlankField(false);
        builder.addField("Plugins", "[>](1) " + NBot.getPluginManager().getPlugins().size(), true);
        builder.addField("JDA version", "[>](1) "+NBot.getJdaVersion(), true);
        builder.addBlankField(false);
        builder.setFooter("this bot use "+NBot.getName() + " v" + NBot.getVersion() + " developped by " + NBot.getAuthor(), null);
        builder.setColor(Color.RED);
        channel.sendMessageToChannel(builder.build());
    }

    @Command(name = "deleteCommand", description = "Deletes the user's command when executed.", powers = 100)
    private void onDeleteCommand(User user, String[] args, Channel channel, Guild guild, SimpleCommand simpleCommand)
    {
        if (args.length == 0) {
            channel.sendMessageToChannel(user.getAsMention() + ", " + guild.getPrefix() + simpleCommand.getSimpleName() + " <true[-t]|false[-f]>");
            return;
        }

        if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("-t"))
        {
            guild.setDeleteCommand(true);
            channel.sendMessageToChannel(user.getAsMention()+", the commands are now deleted after execution.");
            return;
        }
        if (args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("-f"))
        {
            guild.setDeleteCommand(false);
            channel.sendMessageToChannel(user.getAsMention()+", the commands are no longer deleted after execution.");
            return;
        }

        channel.sendMessageToChannel(user.getAsMention() + ", " + guild.getPrefix() + simpleCommand.getSimpleName() + " <true[-t]|false[-f]>");
    }

    @Command(name = "cmdchan", description = "Set channels for command.", powers = 100)
    private void onCmdChan(Channel channel, String[] args, Message message, User user, Guild guild, SimpleCommand simpleCommand)
    {
        if(args.length < 3)
        {
            channel.sendMessageToChannel(user.getAsMention()+", "+guild.getPrefix()+simpleCommand.getSimpleName()+" <add|remove> <commandName> <#Channel>");
            return;
        }

        if(message.getMentionedChannels().isEmpty())
        {
            channel.sendMessageToChannel(user.getAsMention()+", "+guild.getPrefix()+simpleCommand.getSimpleName()+" <add|remove> <commandName> <#Channel>");
            return;
        }

        TextChannel textChannel = message.getMentionedChannels().get(0);

        boolean add;

        if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("-a"))
            add = true;
        else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("-r"))
            add = false;
        else
        {
            channel.sendMessageToChannel(user.getAsMention()+", "+guild.getPrefix()+simpleCommand.getSimpleName()+" <add|remove> <commandName> <#Channel>");
            return;
        }

        SimpleCommand targetCommand = guild.getCommand(args[1].toLowerCase());

        if(targetCommand == null)
        {
            channel.sendMessageToChannel(user.getAsMention()+", command not found !");
            return;
        }

        if(add)
        {
            targetCommand.addChannel(textChannel);
            channel.sendMessageToChannel(user.getAsMention()+", adding channel "+textChannel.getName()+" for the command "+targetCommand.getSimpleName()+" !");
            return;
        }

        targetCommand.removeChannel(textChannel);
        channel.sendMessageToChannel(user.getAsMention()+", removing channel "+textChannel.getName()+" for the command "+targetCommand.getSimpleName()+" !");
    }

    @Command(name = "jda", description = "Send the link from the JDA API in GitHub.", toPrivate = true)
    private void onJDA(Channel channel)
    {
        channel.sendMessageToChannel("https://github.com/DV8FromTheWorld/JDA");
    }

    @Command(name = "slf4j", description = "Send the link from the SLF4J in GitHub.", toPrivate = true)
    private void onSLF4J(Channel channel)
    {
        channel.sendMessageToChannel("https://github.com/qos-ch/slf4j");
    }

    @Command(name = "nbot", description = "Send the link from the NBot API in GitHub.", toPrivate = true)
    private void onNBot(Channel channel)
    {
        channel.sendMessageToChannel("https://github.com/NeutronStars/NBot_v2_0");
    }

    @Command(name = "jdadoc", description = "Send the link from the JDA documentation.", toPrivate = true)
    private void onJDADoc(Channel channel)
    {
        channel.sendMessageToChannel("http://home.dv8tion.net:8080/job/JDA/javadoc/");
    }
}
