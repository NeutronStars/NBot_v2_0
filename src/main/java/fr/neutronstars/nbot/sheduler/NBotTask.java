package fr.neutronstars.nbot.sheduler;

/**
 * Created by NeutronStars on 12/10/2017
 */
class NBotTask
{
    private final Runnable runnable;
    private final long delay, period;
    private final int id;
    private long time;

    protected NBotTask(Runnable runnable, int id, long delay, long period)
    {
        this.runnable = runnable;
        this.period = period;
        this.delay = delay;
        this.id = id;
    }

    protected int getId()
    {
        return id;
    }

    protected void call()
    {
        if(time > delay && time-delay >= period)
        {
            time = period < 0L ? delay + 1L : delay;
            runnable.run();
        }
        time++;
    }

    protected boolean isStopped()
    {
        return period < 0L && time > delay + 1L;
    }
}
