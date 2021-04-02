package stage1;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

import java.util.LinkedList;

public class Crane extends Thread
{
    public static final int MAX_WEIGHT_LIQUID_CRANE = 10;
    public static final int MAX_WEIGHT_CONTAINER_CRANE = 4;
    public static final int MAX_WEIGHT_LOOSE_CRANE = 100;

    public static final int WAIT_FOR_CONTAINER = 5;
    public static final int WAIT_FOR_LOOSE = 20;
    public static final int WAIT_FOR_LIQUID = 8;

    private CargoType type;
    private LinkedList<Task> tasker;
    private Timer simulationTime;
    private Port port;

    Crane(LinkedList<Task> tasker, CargoType type, Timer simulationTime, Port port)
    {
        this.tasker = tasker;
        this.type = type;
        this.simulationTime = simulationTime;
        this.port = port;
        start();
    }

    @Override
    public void run()
    {
        try {
            for(int i = 0; i < tasker.size(); i++)
            {
                Task task = tasker.get(i);

                if(!task.isDone())
                {
                    printCurrentStatus(this.getName(), task.getShip().getName(), "Status: check", simulationTime.getTimerTime());

                    while (!simulationTime.isStop() && task.getComingTime().bigger(simulationTime.getTimerTime()))
                    {
                        this.sleep(1);
                    }
                    if (simulationTime.isStop()){
                        this.interrupt();
                    }

                    TaskerAnswer answer = task.subscribeToExecutions(simulationTime.getTimerTime());

                    switch (answer)
                    {
                        case SUCCESS:
                            printCurrentStatus(this.getName(), task.getShip().getName(), "Unload status SUCCESS", simulationTime.getTimerTime());
                            while (!task.isDone())
                            {
                                int wait = getWait(type);
                                Time newTime = new Time();
                                newTime = newTime.plus(simulationTime.getTimerTime());
                                newTime = newTime.plus(wait);
                                while (!simulationTime.isStop() && !simulationTime.getTimerTime().bigger(newTime))
                                {
                                    this.sleep(1);
                                }
                                if (simulationTime.isStop())
                                {
                                    this.interrupt();
                                }
                                task.unload(getMaxWeight());
                            }
                            if (simulationTime.isStop())
                            {
                                this.interrupt();
                            }
                            else{
                                task.setEmptyTime(simulationTime.getTimerTime());
                            }
                            printCurrentStatus(this.getName(), task.getShip().getName(), "Change Empty time", simulationTime.getTimerTime());
                            break;
                        case FAIL:
                            printCurrentStatus(this.getName(), task.getShip().getName(), "Status: unload fail", simulationTime.getTimerTime());
                            break;
                    }
                }
            }
        }
        catch (InvalidValue e)
        {
            simulationTime.stop();
            System.out.format("%15s%30s", this.getName(), "Simulation time end");
            System.out.println();
            this.interrupt();
        }
        catch(InterruptedException ex)
        {
            ex.fillInStackTrace();
            System.out.println("Exception");
        }
        finally
        {
            port.setStat(this);
        }
        simulationEnd();
    }

    private void simulationEnd()
    {
        if (!this.isInterrupted())
        {
            System.out.println(this.getName() + " kill yourself");
            this.interrupt();
        }
    }

    private synchronized void printCurrentStatus(String threadName, String ship, String message, Time time)
    {
        System.out.format("%15s%15s%30s%15s\n", threadName, ship, message, time.toString());
    }

    private int getWait (CargoType type)
    {
        int output = 0;
        switch(type)
        {
            case CONTAINER:
                output = WAIT_FOR_CONTAINER;
                break;
            case LOOSE:
                output = WAIT_FOR_LOOSE;
                break;
            case LIQUID:
                output = WAIT_FOR_LIQUID;
                break;
        }
        return output;
    }

    private synchronized int getTotalCost()
    {
        int cost = 0;
        for (Task task: tasker)
        {
            cost+=task.getExpenses();
        }
        return cost;
    }

    private synchronized void print()
    {
/*        for (Task task: tasker)
        {
            System.out.println(task.toString());
        }*/
        System.out.println("\nTotal cost for "+ type.toString()+": " + getTotalCost());
    }

    private int getMaxWeight()
    {
        int output = 0;
        switch(type)
        {
            case CONTAINER:
                output = MAX_WEIGHT_CONTAINER_CRANE;
                break;
            case LOOSE:
                output = MAX_WEIGHT_LOOSE_CRANE;
                break;
            case LIQUID:
                output = MAX_WEIGHT_LIQUID_CRANE;
                break;
        }
        return output;
    }

}
