package fr.neutronstars.nbot.command;

import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.plugin.NBotPlugin;

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
    private final List<String> aliases = new ArrayList<>();
    private final NBotPlugin plugin;
    private final Method method;
    private final Object object;

    private String customName;
    private int power;

    protected SimpleCommand(String name, String description, int power, List<Long> guilds, List<Long> channels, Method method, Object object, NBotPlugin plugin)
    {
        this.name = name.toLowerCase();
        this.description = description;
        this.power = power;
        this.guilds.addAll(guilds);
        this.channels.addAll(channels);
        this.method = method;
        this.object = object;
        this.plugin = plugin;
    }

    protected SimpleCommand(String name, String description, int power, long[] guilds, long[] channels, Method method, Object object, NBotPlugin plugin)
    {
        this.name = name.toLowerCase();
        this.description = description;
        this.power = power;
        for(long l : guilds) this.guilds.add(l);
        for(long l : channels) this.channels.add(l);
        this.method = method;
        this.object = object;
        this.plugin = plugin;
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

    public List<String> getAliases()
    {
        return aliases;
    }

    public String getSimpleName()
    {
        return customName == null ? name : customName;
    }

    public boolean guildCanExecute(Guild guild)
    {
        return guilds.contains(guild.getIdLong());
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    public void setPower(int power)
    {
        this.power = power;
    }

    public void addAlias(String... aliases)
    {
        for(String alias : aliases)
            if(!this.aliases.contains(alias)) this.aliases.add(alias);
    }

    public void addAlias(Collection<String> aliases)
    {
        for(String alias : aliases)
            if(!this.aliases.contains(alias)) this.aliases.add(alias);
    }

    public String getAliasesToString()
    {
        StringBuilder builder = new StringBuilder();

        for(String alias : aliases)
        {
            if(builder.length() > 0) builder.append(", ");
            builder.append(alias);
        }

        return builder.length() == 0 ? "Not Alias" : builder.toString();
    }

    public void removeAlias(String... aliases)
    {
        for(String alias : aliases)
            if(!this.aliases.contains(alias)) this.aliases.remove(alias);
    }

    public SimpleCommand clone(Guild guild)
    {
        return new SimpleCommand(name, description, power, Arrays.asList(guild.getIdLong()), new ArrayList<>(channels), method, object, plugin);
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
                .append(",\"aliases\":")
                    .append(aliases.toString().replace(" ", "").replace("[", "[\"").replace(",", "\",\"").replace("]", "\"]"))
                .append("}").toString();
    }
}
