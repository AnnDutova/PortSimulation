package stage1.threads;

import stage1.enums.ShipState;
import stage1.ships.Task;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;

public class BlockingQueue
{
    private List<Task> queue = new ArrayList<>();
    private int countOfThreads = 0;

    BlockingQueue(int countOfThreads)
    {
        this.countOfThreads = countOfThreads;
    }

    synchronized public Task get(Crane crane)
    {
        if (countOfThreads == 1)
        {
            return getForOne(crane);
        }
        else{
            return getForMore(crane);
        }
    }

    synchronized private Task getForOne(Crane crane)
    {
        if (!queue.isEmpty())
        {
            Task task = queue.get(0);
            if (task.getShip().getState() != ShipState.EMPTY)
            {
                if (!task.isExecutor(crane))
                {
                    task.setExecutors(crane);
                }
                return task;
            }
            else {
                queue.remove(0);
                return null;
            }
        }
        return null;
    }

    synchronized private Task getForMore(Crane crane)
    {
        if (!queue.isEmpty())
        {
            for (Task task: queue)
            {
                if (task.getShip().getState() != ShipState.EMPTY)
                {
                    if (task.isExecutor(crane))
                    {
                        return task;
                    }
                    else if (task.countOfExecutors() <= 1)
                    {
                        task.setExecutors(crane);
                        return task;
                    }
                }
/*                else {
                    queue.remove(task);
                }*/
            }
            return null;
            /*Task task = queue.get(0);
            if (task.getShip().getState() != ShipState.EMPTY)
            {
                if (task.countOfExecutors() > 2)
                {

                    return null;
                }
                else if (!task.isExecutor(crane) && task.countOfExecutors() <= 1){
                    task.setExecutors(crane);
                    return task;
                }
                else return task;
            }
            else {
                queue.remove(task);
                return null;
            }*/
        }
        return null;
    }

    synchronized public void put(Task task)
    {
        queue.add(task);
    }
}
