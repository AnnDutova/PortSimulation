package stage1;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

import java.util.Random;

import static stage1.Time.MAX_HOURS;

public class Task
{
    private final static int MAX_COUNT = 2;
    private final static int COST = 100;

    private DockedShip ship;
    private int countOfWorkers;

    private Time unloadingTimeStart;
    private Time comingTime;
    private Time emptyTime;
    private Time delay;

    private Random random = new Random();
    private boolean done;

    public Time getUnloadingTimeStart()
    {
        return unloadingTimeStart;
    }

    Task(DockedShip ship)
    {
        this.ship=ship;
        if (random.nextBoolean())
        {
            this.comingTime = ship.getComingTime().plus(ship.getDeviationFromTheSchedule());
        }
        else {
            this.comingTime = ship.getComingTime().minus(ship.getDeviationFromTheSchedule());
        }
        this.unloadingTimeStart = this.comingTime.plus(ship.getWaitingForUnloading());
        this.done = false;
    }

    Task(DockedShip ship, Time comingTime, Time unloadingTimeStart)
    {
        this.ship = ship;
        this.comingTime = comingTime;
        this.unloadingTimeStart = unloadingTimeStart;
        this.done = false;

    }

    public DockedShip getShip()
    {
        return this.ship;
    }

    public Time getComingTime()
    {
        return this.comingTime;
    }

    public synchronized int getDelay()
    {
        if (delay==null)
        {
            return 0;
        }
        return delay.getTimeInSeconds();
    }

    public synchronized TaskerAnswer subscribeToExecutions(Time time)
    {
        if(countOfWorkers < MAX_COUNT)
        {
            countOfWorkers++;
            if (countOfWorkers == 1)
            {
                this.unloadingTimeStart = time.bigger(unloadingTimeStart) ? time : unloadingTimeStart;
            }
            return TaskerAnswer.SUCCESS;
        }
        return TaskerAnswer.FAIL;
    }

    public synchronized void unload( int weight )
    {
        if (!ship.getState().equals(ShipState.EMPTY))
        {
            ship.takePeaceOfCargo(weight);
        }
        else {
            this.done = true;
        }
    }

    public synchronized boolean isDone()
    {
        return done;
    }

    public synchronized void setEmptyTime(Time time)
    {
        this.emptyTime = time;
        this.delay = emptyTime.getDifference(comingTime.plus(ship.getParkingTime()));
    }

    public Time getEmptyTime()
    {
        return this.emptyTime;
    }

    public int getExpenses()
    {
        if(delay!=null)
        {
        return delay.getHour() * COST + delay.getDay() * COST * MAX_HOURS;
        }
        else return 0;
    }

    public Task copy()
    {
        Task newTask = new Task(this.getShip().copy(), this.comingTime, this.unloadingTimeStart);
        return newTask;
    }

    @Override
    public String toString()
    {
        if (emptyTime!= null)
        {
        return ship.toString() +
                "\nComing time: (coming + deviation) " + comingTime.toString() +
                "\nUnloading time start: (coming + CraneWait) " + unloadingTimeStart.toString() +
                "\nEmpty time: " + emptyTime.toString() +
                "\nNew time: (empty - unloading) "  + emptyTime.minus(unloadingTimeStart) +
                "\nCost: " + getExpenses() + "\nDelay: " + getDelay();

        }
        else return ship.toString() +
                "\nComing time: (coming + deviation) " + comingTime.toString() +
                "\nUnloading time start: (coming + CraneWait) " + unloadingTimeStart.toString() +
                "\nCost: " + getExpenses() + "\nNot unload " + "\nDelay: " + getDelay();
    }
}
