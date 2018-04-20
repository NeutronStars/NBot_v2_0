package fr.neutronstars.nbot.command;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.entity.Console;
import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.entity.Message;
import fr.neutronstars.nbot.entity.User;
import fr.neutronstars.nbot.plugin.NBotPlugin;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by NeutronStars on 21/09/2017
 */
public class CommandManager
{
    private static final List<SimpleCommand> defaultCommand = new ArrayList<>();
    private static final Map<String, SimpleCommand> consoleCommand = new HashMap<>();
    private static final Map<String, SimpleCommand> privateCommand = new HashMap<>();

    public static void registerCommands(NBotPlugin plugin, Object... objects)
    {
        for(Object object : objects) registerCommand(object, plugin);
    }

    public static void registerCommand(Object commandManager, NBotPlugin plugin){

        if(commandManager instanceof CommandBuilder)
        {
            SimpleCommand command = new SimpleCommand((CommandBuilder) commandManager, plugin);
            defaultCommand.add(command);

            if(command.canExecute(Command.ExecutorType.CONSOLE)) consoleCommand.put(command.getSimpleName(), command);
            if(command.isPrivate()) privateCommand.put(command.getSimpleName(), command);
            return;
        }

        for(Method method : commandManager.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(Command.class)){
                Command command = method.getAnnotation(Command.class);
                method.setAccessible(true);
                SimpleCommand simpleCommand = new SimpleCommand(command.name(), command.description(), command.powers(), command.guilds(), method, commandManager, command.executor(), command.toPrivate(), command.privateOnly(), plugin);
                defaultCommand.add(simpleCommand);
                if(simpleCommand.canExecute(Command.ExecutorType.CONSOLE))
                    consoleCommand.put(simpleCommand.getSimpleName(), simpleCommand);
                if(simpleCommand.isPrivate())
                    privateCommand.put(simpleCommand.getSimpleName(), simpleCommand);
            }
        }
    }

    protected static Map<String, SimpleCommand> getCommandMap(Guild guild)
    {
        Map<String, SimpleCommand> commandMap = new HashMap<>();

        for(SimpleCommand simpleCommand : defaultCommand)
        {
            if(simpleCommand.isPrivateOnly() || !simpleCommand.canExecute(Command.ExecutorType.USER) || (simpleCommand.getGuilds().size() != 0 && !simpleCommand.guildCanExecute(guild)))
                continue;
            commandMap.put(simpleCommand.getSimpleName(), simpleCommand.clone(guild));
        }

        return commandMap;
    }

    public static void onPrivateCommand(User user, Message message, String command)
    {
        if(command == null) return;
        Object[] object = CommandMap.getCommands(privateCommand, command.split(" "));
        if(object[0] == null) return;

        if(object[0] instanceof List)
        {
            List<String> list = (List<String>) object[0];

            StringBuilder builder = new StringBuilder(32);
            for(int i = 0; i < list.size(); i++)
            {
                if(i != 0) builder.append(", ");
                builder.append(list.get(i));
            }
            builder.append(".");
            user.sendMessageToSender(builder.toString());
            return;
        }

        SimpleCommand simpleCommand = (SimpleCommand)object[0];
        if(simpleCommand.getPlugin() != null)
            simpleCommand.getPlugin().getLogger().info("[Private-Command] "+user.getName()+" -> "+command);
        else
            NBot.getLogger().info("[Private-Command] "+user.getName()+" -> "+command);

        try {
            CommandMap.execute(simpleCommand, command, (String[]) object[1], message, user);
        }catch(Throwable e) {
            if(simpleCommand.getPlugin() != null)
                simpleCommand.getPlugin().getLogger().error(e.getMessage(), e);
            else
                NBot.getLogger().error(e.getMessage(), e);
        }
    }

    public static void onConsoleCommand(Console console, String command)
    {
        if(command == null) return;
        Object[] object = CommandMap.getCommands(consoleCommand, command.split(" "));
        if(object[0] == null) return;

        if(object[0] instanceof List)
        {
            List<String> list = (List<String>) object[0];

            StringBuilder builder = new StringBuilder(32);
            for(int i = 0; i < list.size(); i++)
            {
                if(i != 0) builder.append(", ");
                builder.append(list.get(i));
            }
            builder.append(".");
            console.sendMessageToSender(builder.toString());
            return;
        }

        SimpleCommand simpleCommand = (SimpleCommand)object[0];
        if(simpleCommand.getPlugin() != null)
            simpleCommand.getPlugin().getLogger().info("[Command-Console] -> "+command);
        else
            NBot.getLogger().info("[Command-Console] -> "+command);

        try {
            CommandMap.execute(simpleCommand, command, (String[]) object[1], null, console);
        }catch(Throwable e) {
            if(simpleCommand.getPlugin() != null)
                simpleCommand.getPlugin().getLogger().error(e.getMessage(), e);
            else
                NBot.getLogger().error(e.getMessage(), e);
        }
    }

    public static List<SimpleCommand> getDefaultCommands(boolean console)
    {
        List<SimpleCommand> defaultCommands = new ArrayList<>();
        Collection<SimpleCommand> commands = console ? consoleCommand.values() : privateCommand.values();

        for(SimpleCommand command : commands)
            if(command.getPlugin() == null) defaultCommands.add(command);

        return defaultCommands;
    }

    public static Map<NBotPlugin, List<SimpleCommand>> getPluginCommands(boolean console)
    {
        Map<NBotPlugin, List<SimpleCommand>> pluginCommandMap = new HashMap<>();
        Collection<SimpleCommand> commands = console ? consoleCommand.values() : privateCommand.values();

        for(SimpleCommand command : commands)
        {
            if(command.getPlugin() == null) continue;
            if(!pluginCommandMap.containsKey(command.getPlugin()))
                pluginCommandMap.put(command.getPlugin(), new ArrayList<>());
            pluginCommandMap.get(command.getPlugin()).add(command);
        }
        return pluginCommandMap;
    }
}
