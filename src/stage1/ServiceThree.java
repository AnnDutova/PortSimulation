package stage1;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import java.util.ArrayList;

public class ServiceThree
{
    private Port port;
    private Mutex mutex;
    private ArrayList<Task> tasker;
    private Statistic stat;

    ServiceThree(Mutex mutex)
    {
        this.mutex = mutex;
    }

    public void start()
    {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readJson();
        simulation();

    }

    public void printStatistic()
    {
        stat = port.getStatistic();
        System.out.println(stat.toString());
    }

    private void readJson()
    {
        TimeTableJSONParser parser = new TimeTableJSONParser();
        this.tasker = parser.readJSON();
    }

    private void simulation()
    {
        this.port = new Port(copyTasker(tasker), this);
        port.simulate();
    }

    private void restart()
    {
        port.simulate();
    }

    public void getNewStat(Statistic stat)
    {
        this.stat = stat;
        waitStatistic();
    }

    private void waitStatistic()
    {
        stat = port.getStatistic();
        if (!stat.isOptimal())
        {
            port = new Port(copyTasker(tasker),
                    stat.getOptimalLooseCount(port.getCountOfLooseCrane()),
                    stat.getOptimalLiquidCount(port.getCountOfLiquidCranes()),
                    stat.getOptimalContainerCount(port.getCountOfContainerCranes()), this);
            System.out.println(stat.toString());
            restart();
        }
        else{
            mutex.release();
        }
    }

    private ArrayList<Task> copyTasker (ArrayList<Task> tasker)
    {
        ArrayList<Task> newTasker = new ArrayList<>();
        for (Task task: tasker)
        {
            newTasker.add(task.copy());
        }
        return newTasker;
    }

}
