package stage1.services;

import stage1.enums.CargoType;
import stage1.schedule.TimeTableJSONParser;
import stage1.ships.Task;
import stage1.threads.Port;
import stage1.threads.Statistic;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ServiceThree
{
    private final static int MAX_TRY_NUMBER = 10;
    private Port port;
    private Semaphore mutex;
    private ArrayList<Task> tasker = null;
    private Statistic stat = null;
    private int attemptNumber;

    public ServiceThree(Semaphore mutex)
    {
        this.mutex = mutex;
        attemptNumber = 0;
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

    private void readJson()
    {
        TimeTableJSONParser parser = new TimeTableJSONParser();
        tasker = parser.readJSON();
    }

    public void printStatistic()
    {
        stat = port.getStatistic();
        System.out.println(stat.toString());
    }

    private void simulation()
    {
        this.port = new Port(copyTasker(tasker), this);
        port.simulate();
    }

    private void restart()
    {
        attemptNumber++;
        port.simulate();
    }

    public void setNewStat(Statistic stat)
    {
        this.stat = stat;
        checkStatistic();
    }

    private void checkStatistic()
    {
        if (!stat.isOptimal() && attemptNumber <= MAX_TRY_NUMBER)
        {
            this.port = new Port(copyTasker(tasker),
                    stat.getOptimal(CargoType.LOOSE, port.getCountOfLooseCrane()),
                    stat.getOptimal(CargoType.LIQUID, port.getCountOfLiquidCranes()),
                    stat.getOptimal(CargoType.CONTAINER, port.getCountOfContainerCranes()), this);
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
