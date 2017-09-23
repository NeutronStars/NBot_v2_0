# __**[** *N-Bot API* **]**__

N-Bot API is an __*open-source project*__ using **JDA** which will allow you **create/personalize** your bots simplest through a plugin system.

For use it download the [latest release](https://github.com/NeutronStars/N-Bot/releases) and execute the following command `java -jar N-Bot-VERSION-withDependencies-JDA-VERSION.jar` a **config** folder will appear who contains an **config.json** file. Open it and insert your bot **token**, now you can re-execute the previous command, folders are going to generate. When you want to stop the bot, just print `stop` in the console.

For create a **plugin**, add the **N-Bot API** on your project libraries, your main class will need to extends **NBotPlugin** who contains `onLoad()`, `onRegisterCommands()`, `onEnable()` and `onDisable()` methods with `@Override` annotation.

```java
public class MyPlugin extends NBotPlugin
{
  public MyPlugin()
  {
    super("Author");
  }

  @Override
  public void onCommandRegisters()
  {
     NBot.getLogger().log("Command registered for MyPlugin.");
  }

  @Override
  public void onLoad()
  {
     NBot.getLogger().log("MyPlugin is loaded.");
  }

  @Override
  public void onLoad()
  {
     NBot.getLogger().log("MyPlugin is enabled.");
  }

  @Override
  public void onDisable()
  {
     NBot.getLogger().log("MyPlugin is disabled.");
  }
}
```

You can too create commands, create a class and insert methods with **@Command** annotation like this.

```java
import fr.neutronstars.nbot.entity.User;
import fr.neutronstars.nbot.entity.Channel;
import fr.neutronstars.nbot.entity.Message;

public class MyCommand
{
  @Command(name="info",description="Shows the bot informations.",powers=10)
  public void onInfo(User user, Channel channel, Message message)
  {
      //Your Code.
  }
}
```

and register your command class in the `onCommandRegisters()` method like this.

```java
import fr.neutronstars.nbot.command.CommandManager;

public class MyPlugin extends NBotPlugin
{
  @Override
  public void onCommandRegisters()
  {
     super.registerCommand(MyCommand.class);
  }
}
```

To ensure that your plugin is **valid** you will also have to add a **plugin.json** at your root.

```json
{
  "main":"packages.MainClass",
  "name":"My Plugin",
  "version":"0.0.1-SNAPSHOT"
}
```

Generate your `.jar` and put it in the **plugins** folder.