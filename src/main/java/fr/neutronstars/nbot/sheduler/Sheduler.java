package fr.neutronstars.nbot.sheduler;

import fr.neutronstars.nbot.exception.NBotTaskException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NeutronStars on 12/10/2017
 */
public class Sheduler
{
    private final Map<Integer, NBotTask> taskMap = new HashMap<>();

    private synchronized List<NBotTask> getTasks()
    {
        return new ArrayList<>(taskMap.values());
    }

    public synchronized int runTask(Runnable runnable)
    {
        int id = generateId();
        taskMap.put(id, new NBotTask(runnable, id, 0L, -1L));
        return id;
    }

    public synchronized int runTaskLater(Runnable runnable, long delay)
    {
        int id = generateId();
        taskMap.put(id, new NBotTask(runnable, id, delay, -1L));
        return id;
    }

    public synchronized int runTaskTimer(Runnable runnable, long delay, long period)
    {
        if(period < 1L) throw new NBotTaskException("The period can't be less than 1L.");
        int id = generateId();
        taskMap.put(id, new NBotTask(runnable, id, delay, period));
        return id;
    }

    public synchronized void cancelTask(int task)
    {
        taskMap.remove(task);
    }

    public synchronized void cancelAllTasks()
    {
        taskMap.clear();
    }

    public synchronized void updateTasks()
    {
        for(NBotTask task : getTasks())
        {
            task.call();
            if(task.isStopped()) cancelTask(task.getId());
        }
    }

    private int generateId()
    {
        for(int i = 1; ;i++) if(!taskMap.containsKey(i)) return i;
    }
}
