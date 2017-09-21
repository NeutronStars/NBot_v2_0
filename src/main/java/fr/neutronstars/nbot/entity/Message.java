package fr.neutronstars.nbot.entity;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;

import java.time.OffsetDateTime;
import java.util.Formatter;
import java.util.List;

/**
 * Created by NeutronStars on 20/07/2017
 */
public class Message implements net.dv8tion.jda.core.entities.Message
{
    private final net.dv8tion.jda.core.entities.Message message;
    private boolean deleted;

    public Message(net.dv8tion.jda.core.entities.Message message)
    {
        this.message = message;
    }

    public net.dv8tion.jda.core.entities.Message getMessage()
    {
        return message;
    }

    public List<User> getMentionedUsers()
    {
        return message.getMentionedUsers();
    }

    public boolean isMentioned(User user)
    {
        return message.isMentioned(user);
    }

    public List<TextChannel> getMentionedChannels()
    {
        return message.getMentionedChannels();
    }

    public List<Role> getMentionedRoles()
    {
        return message.getMentionedRoles();
    }

    public boolean mentionsEveryone()
    {
        return message.mentionsEveryone();
    }

    public boolean isEdited()
    {
        return message.isEdited();
    }

    public OffsetDateTime getEditedTime()
    {
        return message.getEditedTime();
    }

    public User getAuthor()
    {
        return message.getAuthor();
    }

    public Member getMember()
    {
        return message.getMember();
    }

    public String getContent()
    {
        return message.getContent();
    }

    public String getRawContent()
    {
        return message.getContent();
    }

    public String getStrippedContent()
    {
        return message.getStrippedContent();
    }

    public boolean isFromType(ChannelType channelType)
    {
        return message.isFromType(channelType);
    }

    public ChannelType getChannelType()
    {
        return message.getChannelType();
    }

    public boolean isWebhookMessage()
    {
        return message.isWebhookMessage();
    }

    public MessageChannel getChannel()
    {
        return message.getChannel();
    }

    public Channel getMessageChannel()
    {
        return new Channel(getChannel());
    }

    public PrivateChannel getPrivateChannel()
    {
        return message.getPrivateChannel();
    }

    public Group getGroup()
    {
        return message.getGroup();
    }

    public TextChannel getTextChannel()
    {
        return message.getTextChannel();
    }

    public Category getCategory()
    {
        return message.getCategory();
    }

    public Guild getGuild()
    {
        return message.getGuild();
    }

    public List<Attachment> getAttachments()
    {
        return message.getAttachments();
    }

    public List<MessageEmbed> getEmbeds()
    {
        return message.getEmbeds();
    }

    public List<Emote> getEmotes()
    {
        return message.getEmotes();
    }

    public List<MessageReaction> getReactions()
    {
        return message.getReactions();
    }

    public boolean isTTS()
    {
        return message.isTTS();
    }

    public RestAction<net.dv8tion.jda.core.entities.Message> editMessage(String s)
    {
        return message.editMessage(s);
    }

    public void editTheMessage(String s)
    {
        editMessage(s).complete();
    }

    public RestAction<net.dv8tion.jda.core.entities.Message> editMessage(MessageEmbed messageEmbed)
    {
        return message.editMessage(messageEmbed);
    }

    public void editTheMessage(MessageEmbed messageEmbed)
    {
        editMessage(messageEmbed).complete();
    }

    public RestAction<net.dv8tion.jda.core.entities.Message> editMessageFormat(String s, Object... objects)
    {
        return message.editMessageFormat(s, objects);
    }

    public void editTheMessageFormat(String s, Object... objects)
    {
        editMessageFormat(s, objects).complete();
    }

    public RestAction<net.dv8tion.jda.core.entities.Message> editMessage(net.dv8tion.jda.core.entities.Message message)
    {
        return this.message.editMessage(message);
    }

    public void editTheMessage(net.dv8tion.jda.core.entities.Message message)
    {
        editMessage(message).complete();
    }

    public AuditableRestAction<Void> delete()
    {
        deleted = true;
        return message.delete();
    }

    public void deleteTheMessage()
    {
        delete().queue();
    }

    public JDA getJDA()
    {
        return message.getJDA();
    }

    public boolean isPinned()
    {
        return message.isPinned();
    }

    public RestAction<Void> pin()
    {
        return message.pin();
    }

    public void pinTheMessage()
    {
        pin().complete();
    }

    public RestAction<Void> unpin()
    {
        return message.unpin();
    }

    public void unpinTheMessage()
    {
        unpin().complete();
    }

    public RestAction<Void> addReaction(Emote emote)
    {
        return message.addReaction(emote);
    }

    public void addReactionToMessage(Emote emote)
    {
        addReaction(emote).queue();
    }

    public void addReactionsToMessage(Emote... emotes)
    {
        for(Emote emote : emotes) addReaction(emote).queue();
    }

    public RestAction<Void> addReaction(String s)
    {
        return message.addReaction(s);
    }

    public void addReactionToMessage(String s)
    {
        addReaction(s).queue();
    }

    public void addReactionsToMessage(String... ss)
    {
        for(String s : ss) addReaction(s).queue();
    }

    public RestAction<Void> clearReactions()
    {
        return message.clearReactions();
    }

    public void clearReactionsToMessage()
    {
        clearReactions().queue();
    }

    public MessageType getType()
    {
        return message.getType();
    }

    public void formatTo(Formatter formatter, int flags, int width, int precision)
    {
        message.formatTo(formatter, flags, width, precision);
    }

    public long getIdLong()
    {
        return message.getIdLong();
    }

    public boolean isDeleted()
    {
        return deleted;
    }
}
