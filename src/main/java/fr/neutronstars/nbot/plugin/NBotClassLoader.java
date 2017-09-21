package fr.neutronstars.nbot.plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by NeutronStars on 01/08/2017
 */
final class NBotClassLoader extends URLClassLoader
{
    private final NBotPlugin plugin;

    protected NBotClassLoader(String main, ClassLoader parent,File file) throws Exception{
        super(new URL[]{file.toURI().toURL()}, parent);
        Class<?> clazz = Class.forName(main, true, this);
        plugin = clazz.asSubclass(NBotPlugin.class).newInstance();
        plugin.setClassLoader(this);
    }

    public NBotPlugin getPlugin() {
        return plugin;
    }
}
