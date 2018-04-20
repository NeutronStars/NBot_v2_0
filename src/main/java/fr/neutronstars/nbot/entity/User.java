package fr.neutronstars.nbot.entity;

import fr.neutronstars.nbot.command.CommandManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.utils.MiscUtil;

import java.util.Formatter;
import java.util.List;

/**
 * Created by NeutronStars on 19/07/2017
 */
public class User extends Channel implements net.dv8tion.jda.core.entities.User, CommandSender
{
    private final net.dv8tion.jda.core.entities.User user;

    public User (net.dv8tion.jda.core.entities.User user)
    {
        super(user);
        this.user = user;
    }

    public net.dv8tion.jda.core.entities.User getUser()
    {
        return user;
    }

    public String getName()
    {
        return user.getName();
    }

    public String getDiscriminator()
    {
        return user.getDiscriminator();
    }

    public String getAvatarId()
    {
        return user.getAvatarId();
    }

    public String getAvatarUrl()
    {
        return user.getAvatarUrl();
    }

    public String getDefaultAvatarId()
    {
        return user.getDefaultAvatarId();
    }

    public String getDefaultAvatarUrl()
    {
        return user.getDefaultAvatarUrl();
    }

    public String getEffectiveAvatarUrl()
    {
        return user.getEffectiveAvatarUrl();
    }

    public boolean hasPrivateChannel()
    {
        return user.hasPrivateChannel();
    }

    public RestAction<PrivateChannel> openPrivateChannel()
    {
        return user.openPrivateChannel();
    }

    public List<Guild> getMutualGuilds()
    {
        return user.getMutualGuilds();
    }

    public boolean isBot()
    {
        return user.isBot();
    }

    public JDA getJDA()
    {
        return user.getJDA();
    }

    public boolean isFake()
    {
        return user.isFake();
    }

    public String getAsMention()
    {
        return user.getAsMention();
    }

    public long getIdLong()
    {
        return user.getIdLong();
    }

    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        boolean leftJustified = (flags & 1) == 1;
        boolean upper = (flags & 2) == 2;
        String out = upper?this.getAsMention().toUpperCase(formatter.locale()):this.getAsMention();
        MiscUtil.appendTo(formatter, width, precision, leftJustified, out);
    }

    public void sendMessageToSender(String message)
    {
        sendMessageToChannel(message);
    }

    public void performCommand(String command)
    {
        CommandManager.onPrivateCommand(this, null, command);
    }

    public boolean isUser()
    {
        return true;
    }

    public String toString()
    {
        return user.toString();
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof User) obj = ((User)obj).getUser();
        return user.equals(obj);
    }
}
