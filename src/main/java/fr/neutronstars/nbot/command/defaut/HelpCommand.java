package fr.neutronstars.nbot.command.defaut;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.command.Command;
import fr.neutronstars.nbot.command.CommandManager;
import fr.neutronstars.nbot.command.SimpleCommand;
import fr.neutronstars.nbot.entity.*;
import fr.neutronstars.nbot.plugin.NBotPlugin;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HelpCommand
{
    @Command(name = "help", description = "Send the commands list.", toPrivate = true, executor = Command.ExecutorType.ALL)
    private void onHelp(CommandSender sender, Channel channel, Guild guild)
    {
        if(sender.isConsole())
        {
            sendConsoleHelp((Console) sender, CommandManager.getDefaultCommands(true), CommandManager.getPluginCommands(true));
            return;
        }

        if(guild == null)
        {
            sendPrivateHelp("Default Commands", (User) sender, CommandManager.getDefaultCommands(false));

            Map<NBotPlugin, List<SimpleCommand>> pluginCommandsMap = CommandManager.getPluginCommands(false);
            for(Map.Entry<NBotPlugin, List<SimpleCommand>> entry : pluginCommandsMap.entrySet())
                sendPrivateHelp(entry.getKey().getName()+" Commands", (User) sender, entry.getValue());
            return;
        }

        channel.sendMessageToChannel(sender.getAsMention()+" check your private message !");

        sendGuildHelp("Default Commands", (User) sender, guild, guild.getDefaultCommands());

        Map<NBotPlugin, List<SimpleCommand>> pluginCommandsMap = guild.getPluginCommands();
        for(Map.Entry<NBotPlugin, List<SimpleCommand>> entry : pluginCommandsMap.entrySet())
            sendGuildHelp(entry.getKey().getName()+" Commands", (User) sender, guild, entry.getValue());
    }

    private void sendGuildHelp(String title, User user, Guild guild, Collection<SimpleCommand> commands)
    {
        if(commands.isEmpty()) return;
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription("Commands for the guild "+guild.getName()+"\n  -> Prefix : "+guild.getPrefix());
        builder.setColor(Color.MAGENTA);
        builder.setFooter(NBot.getName()+" API v"+NBot.getVersion()+" by "+NBot.getAuthor(), NBot.getJDA().getSelfUser().getAvatarUrl());

        for(SimpleCommand command : commands)
        {
            if(!guild.hasPermission(user, command.getPower())) continue;
            String value = "[>](1) Description : "+command.getDescription();
            builder.addField(command.getSimpleName(), value, false);
        }

        user.sendMessageToChannel(builder.build());
    }

    private void sendPrivateHelp(String title, User user, Collection<SimpleCommand> commands)
    {
        if(commands.isEmpty()) return;
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription("Commands for private channel.");
        builder.setColor(Color.GREEN);
        builder.setFooter(NBot.getName()+" API v"+NBot.getVersion()+" by "+NBot.getAuthor(), NBot.getJDA().getSelfUser().getAvatarUrl());

        for(SimpleCommand command : commands)
        {
            String value = "[>](1) Description : "+command.getDescription();
            builder.addField(command.getSimpleName(), value, false);
        }

        user.sendMessageToChannel(builder.build());
    }

    private void sendConsoleHelp(Console console, Collection<SimpleCommand> defaultCommands, Map<NBotPlugin, List<SimpleCommand>> pluginCommands)
    {
        StringBuilder builder = new StringBuilder(32);
        builder.append("\n----------------------------------------");
        builder.append("\n\t\t\tCommands List");
        builder.append("\n----------------------------------------");

        appendCommand(builder, "Default Commands", defaultCommands);

        for(Map.Entry<NBotPlugin, List<SimpleCommand>> entry : pluginCommands.entrySet())
            appendCommand(builder, entry.getKey().getName()+" Commands", entry.getValue());

        builder.append("\n\t").append(NBot.getName()).append(" API v").append(NBot.getVersion()).append(" by ").append(NBot.getAuthor());

        builder.append("\n----------------------------------------");

        console.sendMessageToSender(builder.toString());
    }

    private void appendCommand(StringBuilder builder, String title, Collection<SimpleCommand> commands)
    {
        if(!commands.isEmpty()) {
            builder.append("\n").append(title).append("\n");

            for (SimpleCommand command : commands) {
                builder.append("\n\t-> ").append(command.getSimpleName());
                builder.append("\n\t\tDescription : ").append(command.getDescription());
            }

            builder.append("\n\n----------------------------------------");
        }
    }
}
