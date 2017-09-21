package fr.neutronstars.nbot.plugin;

import fr.neutronstars.nbot.exception.NBotPluginException;

/**
 * Created by NeutronStars on 01/08/2017
 */
public abstract class NBotPlugin
{
    private NBotClassLoader classLoader;
    private final String[] authors;
    private String name, version;

    protected NBotPlugin(String... authors)
    {
        this.authors = authors;
    }

    public String getName()
    {
        return name;
    }

    public String getVersion()
    {
        return version;
    }

    public final String[] getAuthors()
    {
        return authors;
    }

    public String getAuthorsToString()
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; ; i++)
        {
            if(i == authors.length) return builder.length() > 0 ? builder.toString() : "Not author";
            if(i != 0) builder.append(", ");
            builder.append(authors[i]);
        }
    }

    public NBotClassLoader getClassLoader()
    {
        return classLoader;
    }

    protected final void setClassLoader(NBotClassLoader classLoader)
    {
        String error = this.classLoader != null ? "The ClassLoader is already initialized." : classLoader == null ? "The ClassLoader cannot be null." : null;
        if(error != null) throw new NBotPluginException(error);
        this.classLoader = classLoader;
    }

    public final void setName(String name)
    {
        if(this.name != null) throw new NBotPluginException("The plugin name is already initialized.");
        this.name = name;
    }

    public final void setVersion(String version)
    {
        this.version = version;
    }

    public void onLoad(){}

    public void onEnable(){}

    public void onDisable(){}
}
