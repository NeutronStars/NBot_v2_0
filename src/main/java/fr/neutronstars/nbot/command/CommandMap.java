package fr.neutronstars.nbot.command;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.entity.*;
import fr.neutronstars.nbot.entity.Channel;
import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.entity.Message;
import fr.neutronstars.nbot.entity.User;
import fr.neutronstars.nbot.plugin.NBotPlugin;
import fr.neutronstars.nbot.util.Configuration;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NeutronStars on 02/08/2017
 */
public final class CommandMap
{
    private final List<SimpleCommand> commands = new ArrayList<>();
    private final Map<String, SimpleCommand> commandMap;
    private final Configuration commandsDefaultConfig;
    private final Guild guild;

    private String prefix = "bot.";
    private boolean deleteCommand;

    public CommandMap(Guild guild)
    {
        this.guild = guild;
        commandsDefaultConfig = Configuration.loadConfiguration(new File(guild.getFolder(), "commands.json"));

        commandMap = CommandManager.getCommandMap(guild);
        commands.addAll(commandMap.values());

        if(guild.getConfiguration().has("prefix")) prefix = guild.getConfiguration().getString("prefix");
        if(guild.getConfiguration().has("deleteCommand")) deleteCommand = guild.getConfiguration().getBoolean("deleteCommand");

        loadCommands();
    }

    private void loadCommands()
    {
        List<JSONObject> objects = commandsDefaultConfig.getList("commands");
        for(JSONObject object : objects)
        {
            if(object.has("name"))
            {
                SimpleCommand command = commandMap.get(object.getString("name"));
                if(command == null) continue;

                if(object.has("customName"))
                {
                    String customName = object.getString("customName");
                    if(!customName.equalsIgnoreCase("null")) setCustomNameCommand(command.getSimpleName(), customName.toLowerCase());
                }

                if(object.has("power")) command.setPower(object.getInt("power"));

                if(object.has("channels"))
                {
                    JSONArray array = object.getJSONArray("channels");
                    for(int i = 0; i < array.length(); i++)
                    {
                        command.addChannel(array.getLong(i));
                    }
                }
            }
        }
    }

    public Guild getGuild()
    {
        return guild;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public boolean isDeleteCommand()
    {
        return deleteCommand;
    }

    public void setPrefix(String prefix)
    {
        if(prefix == null) prefix = "bot.";
        this.prefix = prefix;
        guild.getConfiguration().set("prefix", prefix);
    }

    public void setDeleteCommand(boolean deleteCommand)
    {
        this.deleteCommand = deleteCommand;
        guild.getConfiguration().set("deleteCommand", deleteCommand);
    }

    public SimpleCommand getCommand(String name)
    {
        return commandMap.get(name);
    }

    public boolean setCustomNameCommand(String lastName, String newName)
    {
        if(newName != null && commandMap.containsKey(newName)) return false;
        SimpleCommand simpleCommand = commandMap.get(lastName);
        if(simpleCommand == null) return false;
        simpleCommand.setCustomName(newName);
        commandMap.remove(lastName);
        if(newName == null) commandMap.put(simpleCommand.getName(), simpleCommand);
        else commandMap.put(newName, simpleCommand);
        return true;
    }

    public void save()
    {
        commandsDefaultConfig.clear();
        JSONArray array = new JSONArray();
        for(SimpleCommand command : commands)
            array.put(new JSONObject(command.toString()));
        commandsDefaultConfig.set("commands", array);
        commandsDefaultConfig.save();
    }

    public boolean onCommand(User user, String command, Message message)
    {
        if(command == null) return false;
        Object[] objects = getCommands(commandMap, command.split(" "));
        if(objects[0] == null) return false;

        if(objects[0] instanceof List)
        {
            List<String> list = (List<String>) objects[0];

            StringBuilder builder = new StringBuilder(32);
            builder.append(user.getAsMention()).append(" -> ");
            for(int i = 0; i < list.size(); i++)
            {
                if(i != 0) builder.append(", ");
                builder.append(list.get(i));
            }
            builder.append(".");
            message.getChannel().sendMessage(builder.toString()).queue();
            return true;
        }

        SimpleCommand simpleCommand = (SimpleCommand)objects[0];
        if(!guild.hasPermission(user, simpleCommand.getPower()) || !simpleCommand.canExecuteToChannel(message.getMessageChannel())) return false;

        if(simpleCommand.getPlugin() != null)
            simpleCommand.getPlugin().getLogger().info("[Command] "+user.getName() + " -> "+command);
        else
            NBot.getLogger().info("[Command] "+user.getName() + " -> "+command);

        try {
            execute(simpleCommand, command, (String[]) objects[1], message, user);
        }catch(Throwable e) {
            if(simpleCommand.getPlugin() != null)
                simpleCommand.getPlugin().getLogger().error(e.getMessage(), e);
            else
               NBot.getLogger().error(e.getMessage(), e);
        }
        return true;
    }

    public static Object[] getCommands(Map<String, SimpleCommand> commandMap, String[] commandSplit)
    {
        if(commandSplit.length == 0) return null;
        String label = commandSplit[0].toLowerCase();
        String[] args = new String[commandSplit.length-1];
        for(int i = 1; i < commandSplit.length; i++) args[i-1] = commandSplit[i];

        SimpleCommand command = commandMap.get(label);
        if(command != null) return new Object[]{command, args};

        List<String> list = new ArrayList<>();
        for(Map.Entry<String, SimpleCommand> entry : commandMap.entrySet())
            if(entry.getKey().toLowerCase().startsWith(label)) list.add(entry.getKey());

        return new Object[]{!list.isEmpty() ? list.size() == 1 ? commandMap.get(list.get(0)) : list : null, args};
    }

    public List<SimpleCommand> getCommands()
    {
        return new ArrayList<>(commands);
    }

    public List<SimpleCommand> getDefaultCommands()
    {
        List<SimpleCommand> defaultCommands = new ArrayList<>();
        List<SimpleCommand> commands = getCommands();

        for(SimpleCommand command : commands)
            if(command.getPlugin() == null) defaultCommands.add(command);

        return defaultCommands;
    }

    public Map<NBotPlugin, List<SimpleCommand>> getPluginCommands()
    {
        Map<NBotPlugin, List<SimpleCommand>> pluginCommandMap = new HashMap<>();
        List<SimpleCommand> commands = getCommands();

        for(SimpleCommand command : commands)
        {
            if(command.getPlugin() == null) continue;
            if(!pluginCommandMap.containsKey(command.getPlugin()))
                pluginCommandMap.put(command.getPlugin(), new ArrayList<>());
            pluginCommandMap.get(command.getPlugin()).add(command);
        }
        return pluginCommandMap;
    }

    protected static void execute(SimpleCommand simpleCommand, String command, String[] args, Message message, CommandSender sender) throws Exception{
        if(simpleCommand.getCommandBuilder() != null)
        {
            CommandArgs commandArgs = new CommandArgs(NBot.getJDA(), command, args, message, message == null ? null : NBot.getGuild(message.getGuild()),
                    message == null ? null : message.getMessageChannel(), sender.isUser() ? (User) sender : null, sender, simpleCommand, message == null ? null : message.getCategory(),
                    message == null || sender.isConsole() ? null : message.getGuild() == null ? null : message.getGuild().getMember((User) sender), sender.isConsole() ? null : (PrivateChannel) ((User)sender).getMessageChannel(),
                    message == null ? null : message.getChannel() instanceof TextChannel ? (TextChannel) message.getChannel() : null, NBot.getJDA().getSelfUser());
            simpleCommand.getCommandBuilder().execute(commandArgs);
            return;
        }

        Parameter[] parameters = simpleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for(int i = 0; i < parameters.length; i++){
            if(parameters[i].getType() == JDA.class) objects[i] = NBot.getJDA();
            else if(parameters[i].getType() == String[].class) objects[i] = args;
            else if(parameters[i].getType() == String.class) objects[i] = command;
            else if(parameters[i].getType() == Message.class || parameters[i].getType() == net.dv8tion.jda.core.entities.Message.class) objects[i] = message;
            else if(parameters[i].getType() == Guild.class || parameters[i].getType() == net.dv8tion.jda.core.entities.Guild.class) objects[i] = message == null ? null : NBot.getGuild(message.getGuild());
            else if(parameters[i].getType() == Channel.class || parameters[i].getType() == MessageChannel.class) objects[i] = message == null ? null : message.getMessageChannel();

            else if(parameters[i].getType() == User.class || parameters[i].getType() == net.dv8tion.jda.core.entities.User.class) objects[i] = sender.isUser() ? (User)sender : null;
            else if(parameters[i].getType() == CommandSender.class) objects[i] = sender;
            else if(parameters[i].getType() == SimpleCommand.class) objects[i] = simpleCommand;

            else if(parameters[i].getType() == Category.class) objects[i] = message == null ? null : message.getCategory();
            else if(parameters[i].getType() == Member.class) objects[i] = message == null || sender.isConsole() ? null : message.getGuild() == null ? null : message.getGuild().getMember((User) sender);

            else if(parameters[i].getType() == PrivateChannel.class) objects[i] = sender.isConsole() ? null : (PrivateChannel) ((User) sender).getMessageChannel();
            else if(parameters[i].getType() == TextChannel.class) objects[i] = message == null ? null : message.getChannel() instanceof TextChannel ? (TextChannel) message.getChannel() : null;

            else if(parameters[i].getType() == SelfUser.class) objects[i] = NBot.getJDA().getSelfUser();
        }

        simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);
    }
}
