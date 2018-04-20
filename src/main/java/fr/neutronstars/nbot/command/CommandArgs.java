package fr.neutronstars.nbot.command;

import fr.neutronstars.nbot.entity.*;
import fr.neutronstars.nbot.entity.Channel;
import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.entity.Message;
import fr.neutronstars.nbot.entity.User;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

public class CommandArgs
{
    private final JDA jda;
    private final String commandToString;
    private final String[] args;
    private final Message message;
    private final Guild guild;
    private final Channel channel;
    private final User user;
    private final CommandSender commandSender;
    private final SimpleCommand simpleCommand;
    private final Category category;
    private final Member member;
    private final PrivateChannel privateChannel;
    private final TextChannel textChannel;
    private final SelfUser selfUser;

    protected CommandArgs(JDA jda, String commandToString, String[] args, Message message, Guild guild, Channel channel, User user, CommandSender commandSender, SimpleCommand simpleCommand, Category category, Member member, PrivateChannel privateChannel, TextChannel textChannel, SelfUser selfUser)
    {
        this.jda = jda;
        this.commandToString = commandToString;
        this.args = args;
        this.message = message;
        this.guild = guild;
        this.channel = channel;
        this.user = user;
        this.commandSender = commandSender;
        this.simpleCommand = simpleCommand;
        this.category = category;
        this.member = member;
        this.privateChannel = privateChannel;
        this.textChannel = textChannel;
        this.selfUser = selfUser;
    }

    public JDA getJda()
    {
        return jda;
    }

    public User getUser()
    {
        return user;
    }

    public TextChannel getTextChannel()
    {
        return textChannel;
    }

    public String[] getArgs()
    {
        return args;
    }

    public String getCommand()
    {
        return commandToString;
    }

    public SimpleCommand getSimpleCommand()
    {
        return simpleCommand;
    }

    public SelfUser getSelfUser()
    {
        return selfUser;
    }

    public PrivateChannel getPrivateChannel()
    {
        return privateChannel;
    }

    public Member getMember()
    {
        return member;
    }

    public Guild getGuild()
    {
        return guild;
    }

    public CommandSender getCommandSender()
    {
        return commandSender;
    }

    public Channel getChannel()
    {
        return channel;
    }

    public Category getCategory()
    {
        return category;
    }

    public Message getMessage()
    {
        return message;
    }
}
