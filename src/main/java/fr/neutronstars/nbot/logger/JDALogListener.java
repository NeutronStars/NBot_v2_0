package fr.neutronstars.nbot.logger;

import net.dv8tion.jda.core.utils.SimpleLog;
import net.dv8tion.jda.core.utils.SimpleLog.LogListener;

/**
 * Created by NeutronStars on 31/07/2017
 */
public class JDALogListener implements LogListener
{
    private final NBotLogger logger = NBotLogger.getLogger("JDA");

    public void onLog(SimpleLog log, org.slf4j.event.Level logLevel, Object message)
    {
        logger.log(Level.valueOf(logLevel.toString()), message.toString(), false);
    }

    public void onError(SimpleLog log, Throwable err)
    {
        logger.logThrowable(err, false);
    }
}
