package fr.neutronstars.nbot.entity;

import fr.neutronstars.nbot.NBot;
import net.dv8tion.jda.core.JDA;

/**
 * Created by NeutronStars on 20/07/2017
 */
public interface CommandSender
{
    public void sendMessageToSender(String message);

    public String getName();

    public void performCommand(String command);

    public default JDA getJDA()
    {
        return NBot.getJDA();
    }

    public String getAsMention();

    public default boolean isConsole()
    {
        return false;
    }

    public default boolean isUser()
    {
        return false;
    }
}
