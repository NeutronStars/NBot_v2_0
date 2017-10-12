package fr.neutronstars.nbot.sheduler;

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
        NBot.getSheduler().runTask(this);
    }

    public final void runTaskLater(long delay)
    {
        NBot.getSheduler().runTaskLater(this, delay);
    }

    public final void runTaskTimer(long delay, long period)
    {
        id = NBot.getSheduler().runTaskTimer(this, delay, period);
    }

    public void cancel()
    {
        if(id == -1) throw new NBotTaskException("This task is not started.");
        NBot.getSheduler().cancelTask(id);
        id = -1;
    }
}
