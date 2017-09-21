package fr.neutronstars.nbot.command;

import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.plugin.NBotPlugin;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by NeutronStars on 21/09/2017
 */
public class CommandManager
{
    private static final List<SimpleCommand> defaultCommand = new ArrayList<>();

    public static void registerCommands(NBotPlugin plugin, Object... objects)
    {
        for(Object object : objects) registerCommand(object, plugin);
    }

    public static void registerCommand(Object commandManager, NBotPlugin plugin){
        for(Method method : commandManager.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(Command.class)){
                Command command = method.getAnnotation(Command.class);
                method.setAccessible(true);
                SimpleCommand simpleCommand = new SimpleCommand(command.name(), command.description(), command.powers(), command.guilds(), command.channels(), method, commandManager, plugin);
                defaultCommand.add(simpleCommand);
            }
        }
    }

    protected static Map<String, SimpleCommand> getCommandMap(Guild guild)
    {
        Map<String, SimpleCommand> commandMap = new HashMap<>();

        for(SimpleCommand simpleCommand : defaultCommand)
        {
            if(simpleCommand.getGuilds().size() != 0 && !simpleCommand.guildCanExecute(guild)) continue;
            commandMap.put(simpleCommand.getSimpleName(), simpleCommand.clone(guild));
        }

        return commandMap;
    }

}
