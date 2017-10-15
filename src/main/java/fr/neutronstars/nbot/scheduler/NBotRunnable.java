package fr.neutronstars.nbot.scheduler;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.exception.NBotTaskException;

/**
 * Created by NeutronStars on 12/10/2017
 */
public abstract class NBotRunnable implements Runnable
{
    private int id = -1;

    public final void runTask()
    {
        NBot.getScheduler().runTask(this);
    }

    public final void runTaskLater(long delay)
    {
        NBot.getScheduler().runTaskLater(this, delay);
    }

    public final void runTaskTimer(long delay, long period)
    {
        id = NBot.getScheduler().runTaskTimer(this, delay, period);
    }

    public void cancel()
    {
        if(id == -1) throw new NBotTaskException("This task is not started.");
        NBot.getScheduler().cancelTask(id);
        id = -1;
    }
}
