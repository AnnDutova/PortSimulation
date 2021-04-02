package stage1;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class Port
{
    private LinkedList<Task> shipsWithLooseCargo = new LinkedList<>();
    private LinkedList<Task> shipsWithContainerCargo = new LinkedList<>();
    private LinkedList<Task> shipsWithLiquidCargo = new LinkedList<>();

    private LinkedList<Thread> craneList = new LinkedList<>();

    private int countOfLooseCrane;
    private int countOfLiquidCranes;
    private int countOfContainerCranes;

    private ServiceThree three;
    private Timer simulationTime;

    Port(ArrayList<Task> tasker, ServiceThree three)
    {
        this.countOfContainerCranes = 1;
        this.countOfLiquidCranes = 1;
        this.countOfLooseCrane = 1;
        fillCraneLists(tasker);
        this.three = three;
    }

    Port( ArrayList<Task> tasker, int countOfLooseCrane, int countOfLiquidCranes, int countOfContainerCranes, ServiceThree three)
    {
        this.countOfLooseCrane = countOfLooseCrane;
        this.countOfLiquidCranes = countOfLiquidCranes;
        this.countOfContainerCranes = countOfContainerCranes;
        fillCraneLists(tasker);
        this.three = three;
    }

    public void simulate()
    {

        this.simulationTime = new Timer();
        //Timer timer = new Timer();
        Thread thread = new Thread(simulationTime);
        thread.start();

        startThread(simulationTime);

    }

    public void startThread(Timer timer)
    {
        System.out.println();
        System.out.println("loose "+ countOfLooseCrane + "liq "+ countOfLiquidCranes + "Contain "+ countOfContainerCranes);

        int looseCount = countOfLooseCrane;
        while(looseCount > 0)
        {
            Thread craneLoose = new Crane(shipsWithLooseCargo, CargoType.LOOSE, timer, this);
            looseCount--;
            craneList.add(craneLoose);
        }
        int liquidCount = countOfLiquidCranes;
        while (liquidCount > 0)
        {
            Thread craneLiquid = new Crane(shipsWithLiquidCargo, CargoType.LIQUID, timer, this);
            liquidCount--;
            craneList.add(craneLiquid);
        }
        int containerCranes = countOfContainerCranes;
        while (containerCranes > 0)
        {
            Thread craneContainer = new Crane(shipsWithContainerCargo, CargoType.CONTAINER, timer, this);
            containerCranes--;
            craneList.add(craneContainer);
        }
    }

    public Statistic getStatistic()
    {
        Statistic stat = new Statistic();
        return stat.collectStatistic(shipsWithLooseCargo, shipsWithLiquidCargo, shipsWithContainerCargo);
    }

    public void setStat(Thread thread)
    {
        craneList.remove(thread);
        if (craneList == null || craneList.isEmpty())
        {
            three.getNewStat(getStatistic());
            simulationTime.stop();
            for (Thread crane: craneList)
            {
                crane.interrupt();
            }
        }
    }

    private void fillCraneLists(ArrayList<Task> tasker)
    {
        for(int index = 0; index < tasker.size(); index++ )
        {
            CargoType type = tasker.get(index).getShip().getCargoType();
            Task task = new Task(tasker.get(index).getShip());
            switch(type)
            {
                case LOOSE:
                    shipsWithLooseCargo.add(task);
                    break;
                case LIQUID:
                    shipsWithLiquidCargo.add(task);
                    break;
                case CONTAINER:
                    shipsWithContainerCargo.add(task);
                    break;
            }
        }
        shipsWithLooseCargo.sort(new TaskComparator());
        shipsWithContainerCargo.sort(new TaskComparator());
        shipsWithLiquidCargo.sort(new TaskComparator());
    }

    public int getCountOfLooseCrane()
    {
        return countOfLooseCrane;
    }

    public int getCountOfLiquidCranes()
    {
        return countOfLiquidCranes;
    }

    public int getCountOfContainerCranes()
    {
        return countOfContainerCranes;
    }

}
