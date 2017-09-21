package fr.neutronstars.nbot.logger;

import net.dv8tion.jda.core.utils.SimpleLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by NeutronStars on 31/07/2017
 */
public final class NBotLogger
{

    private static final Map<String, NBotLogger> loggers = new HashMap<>();
    private static final List<String> messages = new ArrayList<>();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy-HH:mm:ss");;
    private static String pattern = "[%1$s] [%2$s] %3$s : %4$s";

    static{
        SimpleLog.addListener(new JDALogListener());
    }

    public static NBotLogger getLogger(String name)
    {
        if(!loggers.containsKey(name)) loggers.put(name, new NBotLogger(name));
        return loggers.get(name);
    }

    public static String getPattern()
    {
        return pattern;
    }

    public static String getDateFormat()
    {
        return simpleDateFormat.toPattern();
    }

    public static void setDateFormat(String dateFormat)
    {
        simpleDateFormat = new SimpleDateFormat(dateFormat);
    }

    public static void setPattern(String pattern)
    {
        NBotLogger.pattern = pattern;
    }

    public static void save(String folder, String name) throws IOException
    {
        File folderFile = new File(folder);
        if(!folderFile.exists()) folderFile.mkdir();
        File file = new File(folderFile, name+".log");
        int number = 1;
        while(file.exists())
        {
            file = new File(folderFile, name+" ("+number+").log");
            number++;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for(String line : messages)
        {
            writer.write(line);
            writer.newLine();
        }
        writer.flush();
        writer.close();
        messages.clear();
    }

    private final String name;

    private NBotLogger(String name)
    {
        this.name = name;
    }

    public void log(String msg)
    {
        log(Level.INFO, msg);
    }

    public void log(Level level, String msg)
    {
        log(level, msg, true);
    }

    public void log(Level level, String msg, boolean show)
    {
        if(level == Level.DEBUG || level == Level.TRACE) return;

        String line = String.format(pattern, nowTimeFormat(), name, level, msg);
        messages.add(line);
        if(show) System.out.println(line);
    }

    public void logThrowable(Throwable throwable)
    {
        logThrowable(throwable, true);
    }

    public void logThrowable(Throwable throwable, boolean show)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(throwable);
        for (StackTraceElement traceElement : throwable.getStackTrace())
            builder.append("\n\tat ").append(traceElement);
        log(Level.FATAL, builder.toString(), show);
    }

    public String nowTimeFormat()
    {
        return simpleDateFormat.format(new Date());
    }
}
