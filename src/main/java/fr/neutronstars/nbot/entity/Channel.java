package fr.neutronstars.nbot.entity;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.UserImpl;
import net.dv8tion.jda.core.requests.RestAction;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeutronStars on 19/07/2017
 */
public class Channel implements MessageChannel
{
    private final MessageChannel messageChannel;

    public Channel(MessageChannel messageChannel)
    {
        this.messageChannel = messageChannel;
    }

    protected Channel(User user)
    {
        messageChannel = user.openPrivateChannel().complete();
    }

    public MessageChannel getMessageChannel()
    {
        return messageChannel;
    }

    public long getLatestMessageIdLong()
    {
        return messageChannel.getLatestMessageIdLong();
    }

    public boolean hasLatestMessage()
    {
        return messageChannel.hasLatestMessage();
    }

    public String getName()
    {
        return messageChannel.getName();
    }

    public ChannelType getType()
    {
        return messageChannel.getType();
    }

    public JDA getJDA()
    {
        return messageChannel.getJDA();
    }

    public long getIdLong()
    {
        return messageChannel.getIdLong();
    }

    public RestAction<Message> sendMessage(String text)
    {
        return messageChannel.sendMessage(text);
    }

    public RestAction<Message> sendMessage(Message msg)
    {
        return messageChannel.sendMessage(msg);
    }

    public RestAction<Message> sendMessageFormat(String format, Object... args)
    {
        return messageChannel.sendMessageFormat(format, args);
    }

    public void sendMessageToChannel(String message)
    {
        messageChannel.sendMessage(message).queue();
    }

    public void sendMessageToChannel(net.dv8tion.jda.core.entities.Message message)
    {
        messageChannel.sendMessage(message).queue();
    }

    public void sendMessageToChannel(MessageEmbed embed)
    {
        messageChannel.sendMessage(embed).queue();
    }

    public void sendMessageFormatToChannel(String format, Object... args)
    {
        sendMessageToChannel(String.format(format, args));
    }

    public void sendFileToChannel(File file, Message message)
    {
        messageChannel.sendFile(file, message).queue();
    }

    public void sendFileToChannel(File file, String fileName, Message message)
    {
        messageChannel.sendFile(file, fileName, message).queue();
    }

    public void sendFileToChannel(InputStream data, String fileName, Message message)
    {
        messageChannel.sendFile(data, fileName, message).queue();
    }

    public void sendFileToChannel(byte[] data, String fileName, Message message)
    {
        messageChannel.sendFile(data, fileName, message).queue();
    }

    public fr.neutronstars.nbot.entity.Message getMessageByIdToChannel(String messageId)
    {
        return new fr.neutronstars.nbot.entity.Message(getMessageById(messageId).complete());
    }

    public fr.neutronstars.nbot.entity.Message getMessageByIdToChannel(long messageId)
    {
        return new fr.neutronstars.nbot.entity.Message(getMessageById(messageId).complete());
    }

    public void deleteMessageByIdToChannel(String messageId)
    {
        messageChannel.deleteMessageById(messageId).queue();
    }

    public void deleteMessageByIdToChannel(long messageId)
    {
        messageChannel.deleteMessageById(messageId).queue();
    }

    public MessageHistory getHistoryAroundToChannel(Message message, int limit)
    {
        return messageChannel.getHistoryAround(message, limit).complete();
    }

    public MessageHistory getHistoryAroundToChannel(String messageId, int limit)
    {
        return messageChannel.getHistoryAround(messageId, limit).complete();
    }

    public MessageHistory getHistoryAroundToChannel(long messageId, int limit)
    {
        return messageChannel.getHistoryAround(messageId, limit).complete();
    }

    public void sendTypingToChannel()
    {
        messageChannel.sendTyping().complete();
    }

    public void addReactionByIdToChannel(String messageId, String unicode)
    {
        messageChannel.addReactionById(messageId, unicode).queue();
    }

    public void addReactionByIdToChannel(long messageId, String unicode)
    {
        messageChannel.addReactionById(messageId, unicode).queue();
    }

    public void addReactionByIdToChannel(String messageId, Emote emote)
    {
        messageChannel.addReactionById(messageId, emote).queue();
    }

    public void addReactionByIdToChannel(long messageId, Emote emote)
    {
        messageChannel.addReactionById(messageId, emote).queue();
    }

    public void pinMessageByIdToChannel(String messageId)
    {
        pinMessageById(messageId).complete();
    }

    public void pinMessageByIdToChannel(long messageId)
    {
        messageChannel.pinMessageById(messageId).complete();
    }

    public void unpinMessageByIdToChannel(String messageId)
    {
        messageChannel.unpinMessageById(messageId).complete();
    }

    public void unpinMessageByIdToChannel(long messageId)
    {
        messageChannel.unpinMessageById(messageId).complete();
    }

    public List<fr.neutronstars.nbot.entity.Message> getPinnedMessagesToChannel()
    {
        List<fr.neutronstars.nbot.entity.Message> messages = new ArrayList<>();
        for(Message message : messageChannel.getPinnedMessages().complete()) messages.add(new fr.neutronstars.nbot.entity.Message(message));
        return messages;
    }

    public fr.neutronstars.nbot.entity.Message editMessageByIdToChannel(String messageId, String newContent)
    {
        return new fr.neutronstars.nbot.entity.Message(editMessageById(messageId, newContent).complete());
    }

    public fr.neutronstars.nbot.entity.Message editMessageByIdToChannel(String messageId, Message newContent)
    {
        return new fr.neutronstars.nbot.entity.Message(editMessageById(messageId, newContent).complete());
    }

    public fr.neutronstars.nbot.entity.Message editMessageFormatByIdToChannel(String messageId, String format, Object... args)
    {
        return new fr.neutronstars.nbot.entity.Message(editMessageFormatById(messageId, format, args).complete());
    }

    public fr.neutronstars.nbot.entity.Message editMessageFormatByIdToChannel(long messageId, String format, Object... args)
    {
        return new fr.neutronstars.nbot.entity.Message(editMessageFormatById(messageId, format, args).complete());
    }

    public fr.neutronstars.nbot.entity.Message editMessageByIdToChannel(long messageId, Message newContent)
    {
        return new fr.neutronstars.nbot.entity.Message(editMessageById(messageId, newContent).complete());
    }

    public fr.neutronstars.nbot.entity.Message editMessageByIdToChannel(String messageId, MessageEmbed newContent)
    {
        return new fr.neutronstars.nbot.entity.Message(editMessageById(messageId, newContent).complete());
    }

    public fr.neutronstars.nbot.entity.Message editMessageByIdToChannel(long messageId, MessageEmbed newContent)
    {
        return new fr.neutronstars.nbot.entity.Message(editMessageById(messageId, newContent).complete());
    }
}
