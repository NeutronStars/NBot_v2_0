package fr.neutronstars.nbot.command;

import fr.neutronstars.nbot.plugin.NBotPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandBuilder
{
    private final String name, description;
    private final List<Long> guilds = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final Command.ExecutorType executor;
    private final boolean toPrivate, privateOnly;

    private int power;
    private Consumer<CommandArgs> consumer;

    public CommandBuilder(String name, String description)
    {
        this(name, description, 0);
    }

    public CommandBuilder(String name, String description, int power)
    {
        this(name, description, 0, Command.ExecutorType.USER, false, false);
    }

    public CommandBuilder(String name, String description, boolean toPrivate, boolean privateOnly)
    {
        this(name, description, 0, toPrivate, privateOnly);
    }

    public CommandBuilder(String name, String description, int power, boolean toPrivate, boolean privateOnly)
    {
        this(name, description, 0, Command.ExecutorType.USER, toPrivate, privateOnly);
    }

    public CommandBuilder(String name, String description, int power, Command.ExecutorType executor, boolean toPrivate, boolean privateOnly)
    {
        this.name = name;
        this.description = description;
        this.power = power;
        this.executor = executor;
        this.toPrivate = toPrivate;
        this.privateOnly = privateOnly;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public Command.ExecutorType getExecutor()
    {
        return executor;
    }

    public int getPower()
    {
        return power;
    }

    public List<Long> getGuilds()
    {
        return guilds;
    }

    public List<String> getAliases()
    {
        return aliases;
    }

    public CommandBuilder addGuild(long guildId)
    {
        guilds.add(guildId);
        return this;
    }

    public boolean isToPrivate()
    {
        return toPrivate;
    }

    public boolean isPrivateOnly()
    {
        return privateOnly;
    }

    public CommandBuilder setExecutor(Consumer<CommandArgs> consumer)
    {
        this.consumer = consumer;
        return this;
    }

    protected void execute(CommandArgs args)
    {
        if(consumer != null) consumer.accept(args);
    }

    public void register(NBotPlugin plugin)
    {
        if(plugin == null)
            CommandManager.registerCommand(this, null);
        else
            plugin.registerCommand(this);
    }
}
