package fr.neutronstars.nbot.util;

import fr.neutronstars.nbot.exception.NBotConfigurationException;
import fr.neutronstars.nbot.logger.NBotLogger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeutronStars on 31/07/2017
 */
public class Configuration
{
    private final File file;

    private JSONObject object;

    private Configuration(File file, JSONObject object)
    {
        this.object = object;
        this.file = file;
    }

    public Object get(String key)
    {
        return object.has(key) ? object.get(key) : null;
    }

    public String getString(String key)
    {
        return object.has(key) ? object.getString(key) : null;
    }

    public int getInt(String key)
    {
        return object.has(key) ? object.getInt(key) : 0;
    }

    public long getLong(String key)
    {
        return object.has(key) ? object.getLong(key) : 0;
    }

    public double getDouble(String key)
    {
        return object.has(key) ? object.getDouble(key) : 0;
    }

    public boolean getBoolean(String key)
    {
        return object.has(key) ? object.getBoolean(key) : false;
    }

    public <T> List<T> getList(String key)
    {
        List<T> list = new ArrayList<>();

        if(object.has(key))
        {
            JSONArray array = object.getJSONArray(key);
            for(int i = 0; i < array.length(); i++)
                list.add((T) array.get(i));
        }

        return list;
    }

    public <E extends Enum<E>> E getEnum(Class<E> clazz, String key)
    {
        return object.has(key) ? object.getEnum(clazz, key) : null;
    }

    public boolean has(String key)
    {
        return object.has(key);
    }

    public void set(String key, Object value)
    {
        object.put(key, value);
    }

    public void clear()
    {
        object = new JSONObject();
    }

    public void save()
    {
        try(JSONWriter writer = new JSONWriter(file))
        {
            writer.write(object);
            writer.flush();
        }
        catch(IOException ioe)
        {
            NBotLogger.getLogger("NBot").logThrowable(new NBotConfigurationException(ioe));
        }
    }

    public static Configuration loadConfiguration(File file)
    {
        if(!file.exists()) return new Configuration(file, new JSONObject());

        try
        {
            return new Configuration(file, new JSONReader(file).toJSONObject());
        }
        catch(IOException ioe)
        {
            NBotLogger.getLogger("NBot").logThrowable(new NBotConfigurationException(ioe));
        }
        return null;
    }
}
