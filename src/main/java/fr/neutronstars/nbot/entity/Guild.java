package fr.neutronstars.nbot.entity;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.command.CommandMap;
import fr.neutronstars.nbot.command.SimpleCommand;
import fr.neutronstars.nbot.plugin.NBotPlugin;
import fr.neutronstars.nbot.util.Configuration;
import fr.neutronstars.nbot.util.JSONReader;
import fr.neutronstars.nbot.util.JSONWriter;
import net.dv8tion.jda.client.requests.restaction.pagination.MentionPaginationAction;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.Region;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.GuildManager;
import net.dv8tion.jda.core.managers.GuildManagerUpdatable;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.restaction.MemberAction;
import net.dv8tion.jda.core.requests.restaction.pagination.AuditLogPaginationAction;
import net.dv8tion.jda.core.utils.cache.MemberCacheView;
import net.dv8tion.jda.core.utils.cache.SnowflakeCacheView;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by NeutronStars on 19/07/2017
 */
public class Guild implements net.dv8tion.jda.core.entities.Guild
{
    private final net.dv8tion.jda.core.entities.Guild guild;
    private final CommandMap commandMap;
    private final Map<String, Integer> usersPower;
    private final Map<String, Integer> rolePowers;
    private final Configuration configuration;

    private final File folder;

    public Guild(net.dv8tion.jda.core.entities.Guild guild)
    {
        if(guild == null) throw new NullPointerException("The guild cannot be null.");
        this.guild = guild;

        folder = new File("guilds/"+guild.getId());
        if(!folder.exists()) folder.mkdir();

        configuration = Configuration.loadConfiguration(new File(folder, "config.json"));

        File file = new File(folder, "users.json");
        if(file.exists()) usersPower = JSONReader.toMap(file);
        else usersPower = new HashMap<>();

        File powersRole = new File(folder, "powersRole.json");
        if(powersRole.exists()) this.rolePowers = JSONReader.toMap(powersRole);
        else this.rolePowers = new HashMap<>();

        commandMap = new CommandMap(this);
    }

    public net.dv8tion.jda.core.entities.Guild getGuild()
    {
        return guild;
    }

    @Override
    public RestAction<EnumSet<Region>> retrieveRegions()
    {
        return guild.retrieveRegions();
    }

    @Override
    public RestAction<EnumSet<Region>> retrieveRegions(boolean includeDeprecated)
    {
        return guild.retrieveRegions(includeDeprecated);
    }

    @Override
    public MemberAction addMember(String accessToken, String userId)
    {
        return guild.addMember(accessToken, userId);
    }

    public String getName()
    {
        return guild.getName();
    }

    public String getIconId()
    {
        return guild.getIconId();
    }

    public String getIconUrl()
    {
        return guild.getIconUrl();
    }

    @Override
    public Set<String> getFeatures()
    {
        return guild.getFeatures();
    }

    public String getSplashId()
    {
        return guild.getSplashId();
    }

    public String getSplashUrl()
    {
        return guild.getSplashUrl();
    }

    @Override
    public RestAction<String> getVanityUrl()
    {
        return guild.getVanityUrl();
    }

    public VoiceChannel getAfkChannel()
    {
        return guild.getAfkChannel();
    }

    public TextChannel getSystemChannel()
    {
        return guild.getSystemChannel();
    }

    public Member getOwner()
    {
        return guild.getOwner();
    }

    public Timeout getAfkTimeout()
    {
        return guild.getAfkTimeout();
    }

    public Region getRegion()
    {
        return guild.getRegion();
    }

    @Override
    public String getRegionRaw()
    {
        return guild.getRegionRaw();
    }

    public boolean isMember(User user)
    {
        return guild.isMember(user);
    }

    public Member getSelfMember()
    {
        return guild.getSelfMember();
    }

    public Member getMember(User user)
    {
        return guild.getMember(user);
    }

    public Member getMemberById(String s)
    {
        return guild.getMemberById(s);
    }

    public Member getMemberById(long l)
    {
        return guild.getMemberById(l);
    }

    public List<Member> getMembers()
    {
        return guild.getMembers();
    }

    public List<Member> getMembersByName(String s, boolean b)
    {
        return guild.getMembersByName(s,b);
    }

    public List<Member> getMembersByNickname(String s, boolean b)
    {
        return guild.getMembersByNickname(s, b);
    }

    public List<Member> getMembersByEffectiveName(String s, boolean b)
    {
        return guild.getMembersByEffectiveName(s, b);
    }

    public List<Member> getMembersWithRoles(Role... roles)
    {
        return guild.getMembersWithRoles(roles);
    }

    public List<Member> getMembersWithRoles(Collection<Role> collection)
    {
        return guild.getMembersWithRoles(collection);
    }

    public MemberCacheView getMemberCache()
    {
        return guild.getMemberCache();
    }

    public SnowflakeCacheView<Category> getCategoryCache()
    {
        return guild.getCategoryCache();
    }

    public TextChannel getTextChannelById(String s)
    {
        return guild.getTextChannelById(s);
    }

    public TextChannel getTextChannelById(long l)
    {
        return guild.getTextChannelById(l);
    }

    public List<TextChannel> getTextChannels()
    {
        return guild.getTextChannels();
    }

    public List<TextChannel> getTextChannelsByName(String s, boolean b)
    {
        return guild.getTextChannelsByName(s, b);
    }

    public SnowflakeCacheView<TextChannel> getTextChannelCache()
    {
        return guild.getTextChannelCache();
    }

    public VoiceChannel getVoiceChannelById(String s)
    {
        return guild.getVoiceChannelById(s);
    }

    public VoiceChannel getVoiceChannelById(long l)
    {
        return guild.getVoiceChannelById(l);
    }

    public List<VoiceChannel> getVoiceChannels()
    {
        return guild.getVoiceChannels();
    }

    public List<VoiceChannel> getVoiceChannelsByName(String s, boolean b)
    {
        return guild.getVoiceChannelsByName(s, b);
    }

    public SnowflakeCacheView<VoiceChannel> getVoiceChannelCache()
    {
        return guild.getVoiceChannelCache();
    }

    public Role getRoleById(String s)
    {
        return guild.getRoleById(s);
    }

    public Role getRoleById(long l)
    {
        return guild.getRoleById(l);
    }

    public List<Role> getRoles()
    {
        return guild.getRoles();
    }

    public List<Role> getRolesByName(String s, boolean b)
    {
        return guild.getRolesByName(s, b);
    }

    public SnowflakeCacheView<Role> getRoleCache()
    {
        return guild.getRoleCache();
    }

    public Emote getEmoteById(String s)
    {
        return guild.getEmoteById(s);
    }

    public Emote getEmoteById(long l)
    {
        return guild.getEmoteById(l);
    }

    public List<Emote> getEmotes()
    {
        return guild.getEmotes();
    }

    public List<Emote> getEmotesByName(String s, boolean b)
    {
        return guild.getEmotesByName(s, b);
    }

    public SnowflakeCacheView<Emote> getEmoteCache()
    {
        return guild.getEmoteCache();
    }

    @Nonnull
    @Override
    public RestAction<List<Ban>> getBanList()
    {
        return guild.getBanList();
    }

    public RestAction<Integer> getPrunableMemberCount(int i)
    {
        return guild.getPrunableMemberCount(i);
    }

    public Role getPublicRole()
    {
        return guild.getPublicRole();
    }

    @Nullable
    public TextChannel getDefaultChannel()
    {
        return guild.getDefaultChannel();
    }

    public GuildManager getManager()
    {
        return guild.getManager();
    }

    public GuildManagerUpdatable getManagerUpdatable()
    {
        return guild.getManagerUpdatable();
    }

    public GuildController getController()
    {
        return guild.getController();
    }

    public MentionPaginationAction getRecentMentions()
    {
        return guild.getRecentMentions();
    }

    public AuditLogPaginationAction getAuditLogs()
    {
        return guild.getAuditLogs();
    }

    public RestAction<Void> leave()
    {
        return guild.leave();
    }

    public RestAction<Void> delete()
    {
        return guild.delete();
    }

    public RestAction<Void> delete(String s)
    {
        return guild.delete(s);
    }

    public AudioManager getAudioManager()
    {
        return guild.getAudioManager();
    }

    public JDA getJDA()
    {
        return guild.getJDA();
    }

    public RestAction<List<Invite>> getInvites()
    {
        return guild.getInvites();
    }

    public RestAction<List<Webhook>> getWebhooks()
    {
        return guild.getWebhooks();
    }

    public List<GuildVoiceState> getVoiceStates()
    {
        return guild.getVoiceStates();
    }

    public VerificationLevel getVerificationLevel()
    {
        return guild.getVerificationLevel();
    }

    public NotificationLevel getDefaultNotificationLevel()
    {
        return guild.getDefaultNotificationLevel();
    }

    public MFALevel getRequiredMFALevel()
    {
        return guild.getRequiredMFALevel();
    }

    public ExplicitContentLevel getExplicitContentLevel()
    {
        return guild.getExplicitContentLevel();
    }

    public boolean checkVerification()
    {
        return guild.checkVerification();
    }

    public boolean isAvailable()
    {
        return guild.isAvailable();
    }

    public long getIdLong()
    {
        return guild.getIdLong();
    }

    public File getFolder()
    {
        return folder;
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public boolean isDeleteCommand()
    {
        return commandMap.isDeleteCommand();
    }

    public void setDeleteCommand(boolean deleteCommand)
    {
        commandMap.setDeleteCommand(deleteCommand);
    }

    public int getPermissionUser(User user)
    {
        if(!usersPower.containsKey(user.getId())) setPermission(user, 0);
        return usersPower.get(user.getId());
    }

    public void setPermission(User user, int power)
    {
        usersPower.put(user.getId(), power);
    }

    public int getPermissionRole(Role role)
    {
        if(!rolePowers.containsKey(role.getId())) setPermission(role, 0);
        return rolePowers.get(role.getId());
    }

    public void setPermission(Role role, int power)
    {
        rolePowers.put(role.getId(), power);
    }

    public boolean hasPermission(fr.neutronstars.nbot.entity.User user, int power)
    {
        return hasPermission(user.getUser(), power);
    }

    public boolean hasPermission(User user, int power)
    {
        if(guild.getOwner().equals(getMember(user)) || getPermissionUser(user) >= power) return true;

        for(Role role : getMember(user).getRoles())
            if(role.hasPermission(Permission.ADMINISTRATOR) || getPermissionRole(role) >= power) return true;

        return false;
    }

    public String getPrefix()
    {
        return commandMap.getPrefix();
    }

    public void save()
    {
        configuration.save();
        commandMap.save();

        try(JSONWriter writer = new JSONWriter("guilds/"+guild.getId()+"/users.json");
            JSONWriter powerRoles = new JSONWriter("guilds/"+guild.getId()+"/powersRole.json"))
        {
            save(usersPower, writer);
            save(rolePowers, powerRoles);

            NBot.getLogger().info(getName()+" is saved.");
        }
        catch(IOException ioe)
        {
            NBot.getLogger().error(ioe.getMessage(), ioe);
        }
    }

    public SimpleCommand getCommand(String name)
    {
        return commandMap.getCommand(name);
    }

    public Collection<SimpleCommand> getCommands()
    {
        return commandMap.getCommands();
    }

    public Collection<SimpleCommand> getDefaultCommands()
    {
        return commandMap.getDefaultCommands();
    }

    public Map<NBotPlugin, List<SimpleCommand>> getPluginCommands()
    {
        return commandMap.getPluginCommands();
    }

    public void setPrefix(String newPrefix)
    {
        commandMap.setPrefix(newPrefix);
    }

    public boolean executeCommand(fr.neutronstars.nbot.entity.User user, String command, Message message)
    {
        return commandMap.onCommand(user, command, message);
    }

    public boolean setCustomNameCommand(String lastName, String newName)
    {
        return commandMap.setCustomNameCommand(lastName, newName);
    }

    private void save(Map<String, Integer> map, JSONWriter writer) throws IOException
    {
        JSONObject object = new JSONObject();
        for(String key : map.keySet()) object.put(key, map.get(key));
        writer.write(object);
        writer.flush();
    }


}
