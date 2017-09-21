package fr.neutronstars.nbot.command;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.entity.*;
import fr.neutronstars.nbot.util.Configuration;
import net.dv8tion.jda.core.JDA;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
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
    private String prefix = "bot.";
    private final Guild guild;

    public CommandMap(Guild guild)
    {
        this.guild = guild;
        commandsDefaultConfig = Configuration.loadConfiguration(new File(guild.getFolder(), "commands.json"));

        commandMap = CommandManager.getCommandMap(guild);
        commands.addAll(commandMap.values());

        if(guild.getConfiguration().has("prefix")) prefix = guild.getConfiguration().getString("prefix");

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

                if(object.has("aliases"))
                {
                    JSONArray array = object.getJSONArray("aliases");
                    for(int i = 0; i < array.length(); i++)
                    {
                        String alias = array.getString(i).toLowerCase();
                        if(alias.equalsIgnoreCase("")) continue;
                        if(!commandMap.containsKey(alias))
                        {
                            command.addAlias(alias);
                            commandMap.put(alias, command);
                        }
                    }
                }

                if(object.has("power")) command.setPower(object.getInt("power"));
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

    public void setPrefix(String prefix)
    {
        if(prefix == null) prefix = "bot.";
        this.prefix = prefix;
        guild.getConfiguration().set("prefix", prefix);
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
        Object[] objects = getCommands(command.split(" "));
        if(objects[0] == null) return false;

        SimpleCommand simpleCommand = (SimpleCommand)objects[0];
        if(!guild.hasPermission(user, simpleCommand.getPower())) return false;

        try {
            execute(simpleCommand, command, (String[]) objects[1], message, user);
        }catch(Exception e) {
            NBot.getLogger().logThrowable(e);
        }
        return true;
    }

    public Object[] getCommands(String[] commandSplit)
    {
        if(commandSplit.length == 0) return null;
        String label = commandSplit[0].toLowerCase();
        String[] args = new String[commandSplit.length-1];
        for(int i = 1; i < commandSplit.length; i++) args[i-1] = commandSplit[i];
        return new Object[]{commandMap.get(label), args};
    }

    public List<SimpleCommand> getCommands()
    {
        return new ArrayList<>(commands);
    }

    private void execute(SimpleCommand simpleCommand, String command, String[] args, Message message, User user) throws Exception{
        Parameter[] parameters = simpleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for(int i = 0; i < parameters.length; i++){
            if(parameters[i].getType() == JDA.class) objects[i] = NBot.getJDA();
            else if(parameters[i].getType() == String[].class) objects[i] = args;
            else if(parameters[i].getType() == String.class) objects[i] = command;
            else if(parameters[i].getType() == Message.class) objects[i] = message;
            else if(parameters[i].getType() == Guild.class) objects[i] = message == null ? null : NBot.getGuild(message.getGuild());
            else if(parameters[i].getType() == Channel.class) objects[i] = message == null ? null : message.getMessageChannel();

            else if(parameters[i].getType() == User.class) objects[i] = user;
            else if(parameters[i].getType() == CommandSender.class) objects[i] = user;
            else if(parameters[i].getType() == SimpleCommand.class) objects[i] = simpleCommand;
        }

        simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);
    }
}
