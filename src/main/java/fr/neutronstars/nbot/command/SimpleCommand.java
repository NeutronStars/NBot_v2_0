package fr.neutronstars.nbot.command;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.entity.Channel;
import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.plugin.NBotPlugin;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by NeutronStars on 02/08/2017
 */
public class SimpleCommand
{
    private final String name, description;
    private final List<Long> guilds = new ArrayList<>(), channels = new ArrayList<>();
    private final CommandBuilder commandBuilder;
    private final NBotPlugin plugin;
    private final Method method;
    private final Object object;
    private final Command.ExecutorType executor;
    private final boolean toPrivate, privateOnly;

    private String customName;
    private int power;

    protected SimpleCommand(String name, String description, int power, List<Long> guilds, Method method, Object object, Command.ExecutorType executor, boolean toPrivate, boolean privateOnly, NBotPlugin plugin)
    {
        this.name = name.toLowerCase();
        this.description = description;
        this.power = power;
        this.guilds.addAll(guilds);
        this.method = method;
        this.object = object;
        this.plugin = plugin;
        this.commandBuilder = null;
        this.executor = executor;
        this.toPrivate = toPrivate;
        this.privateOnly = privateOnly;
    }

    protected SimpleCommand(String name, String description, int power, long[] guilds, Method method, Object object, Command.ExecutorType executor, boolean toPrivate, boolean privateOnly, NBotPlugin plugin)
    {
        this.name = name.toLowerCase();
        this.description = description;
        this.power = power;
        for(long l : guilds) this.guilds.add(l);
        this.method = method;
        this.object = object;
        this.plugin = plugin;
        this.commandBuilder = null;
        this.executor = executor;
        this.toPrivate = toPrivate;
        this.privateOnly = privateOnly;
    }

    protected SimpleCommand(CommandBuilder commandBuilder, NBotPlugin plugin)
    {
        this.name = commandBuilder.getName();
        this.description = commandBuilder.getDescription();
        this.power = commandBuilder.getPower();
        this.guilds.addAll(commandBuilder.getGuilds());
        this.method = null;
        this.object = null;
        this.plugin = plugin;
        this.commandBuilder = commandBuilder;
        this.executor = commandBuilder.getExecutor();
        this.toPrivate = commandBuilder.isToPrivate();
        this.privateOnly = commandBuilder.isPrivateOnly();
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public List<Long> getChannels()
    {
        return channels;
    }

    public List<Long> getGuilds()
    {
        return guilds;
    }

    public int getPower()
    {
        return power;
    }

    public Method getMethod()
    {
        return method;
    }

    public CommandBuilder getCommandBuilder()
    {
        return commandBuilder;
    }

    public boolean canExecuteToChannel(Channel channel)
    {
        updateChannels();
        return channels.isEmpty() || channels.contains(channel.getIdLong());
    }

    private void updateChannels()
    {
        if(channels.isEmpty()) return;
        List<Long> clone = new ArrayList<>(channels);
        for(long id : clone)
        {
            if(NBot.getJDA().getTextChannelById(id) == null)
                channels.remove(id);
        }
    }

    public Object getObject()
    {
        return object;
    }

    public NBotPlugin getPlugin()
    {
        return plugin;
    }

    public String getCustomName()
    {
        return customName;
    }

    public String getSimpleName()
    {
        return customName == null ? name : customName;
    }

    public boolean guildCanExecute(Guild guild)
    {
        return guilds.contains(guild.getIdLong());
    }

    public void addChannel(MessageChannel channel)
    {
        if(!channels.contains(channel.getIdLong())) channels.add(channel.getIdLong());
    }

    public void addChannel(long id)
    {
        if(!channels.contains(id)) channels.add(id);
    }

    public void removeChannel(MessageChannel channel)
    {
        channels.remove(channel.getIdLong());
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    public void setPower(int power)
    {
        this.power = power;
    }

    public boolean isPrivate()
    {
        return toPrivate;
    }

    public boolean isPrivateOnly()
    {
        return privateOnly;
    }

    public boolean canExecute(Command.ExecutorType type)
    {
        return executor == Command.ExecutorType.ALL || executor == type;
    }

    public SimpleCommand clone(Guild guild)
    {
        if(commandBuilder != null) return new SimpleCommand(commandBuilder, plugin);
        return new SimpleCommand(name, description, power, Arrays.asList(guild.getIdLong()), method, object, executor, toPrivate, privateOnly, plugin);
    }

    public String toString()
    {
        return new StringBuilder()
                .append("{\"name\":\"").append(name)
                .append("\",\"customName\":\"").append(customName)
                .append("\",\"description\":\"").append(description)
                .append("\",\"power\":").append(power)
                .append(",\"guilds\":").append(guilds.toString())
                .append(",\"channels\":").append(channels.toString())
                .append("}").toString();
    }
}
