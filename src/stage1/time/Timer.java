package stage1.time;

import stage1.threads.Crane;
import stage1.threads.Port;
import static stage1.threads.Port.*;

import java.util.ArrayList;
import java.util.TreeSet;

public class Timer implements Runnable
{
    private boolean stop = false;
    private int time = 0;
    private Port manager = null;
    private ArrayList<Crane> listeners = null;
    private TreeSet<Integer> timeSet = null;

    public Timer(Port manager)
    {
        this.manager = manager;
        this.timeSet = new TreeSet<>();
    }

    @Override
    public void run()
    {
        if (!stop)
        {
            if (!timeSet.isEmpty())
            {
                time = timeSet.first();
                if (time <= MAX_TIME)
                {
                    manager.update(time);
                    timeSet.remove(time);
                }
                else{
                    killYourself();
                }
            }
            else
            {
                killYourself();
            }
        }
    }

    public void killYourself()
    {
        System.out.println("I die");
        this.stop = true;
    }

    synchronized public boolean isStop(){
        return stop;
    }

    synchronized public void add(int comingTime)
    {
        timeSet.add(comingTime);
    }
}
