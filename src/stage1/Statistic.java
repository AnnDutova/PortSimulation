package stage1;

import java.util.LinkedList;

public class Statistic
{
    public final static int CRANE_COST = 30000;

    private int totalCount;

    private int looseCost;
    private int liquidCost;
    private int containerCost;

    private int totalCost;

    private int averageQueueLengthLoose;
    private int averageQueueLengthLiquid;
    private int averageQueueLengthContainer;

    private int averageDeviationLoose;
    private int averageDeviationLiquid;
    private int averageDeviationContainer;

    private int averageWaitingTimeLoose;
    private int averageWaitingTimeLiquid;
    private int averageWaitingTimeContainer;

    private int maxDeviationLoose;
    private int maxDeviationLiquid;
    private int maxDeviationContainer;

    Statistic()
    {
        this.totalCount = 0;
        this.totalCost = 0;
        this.looseCost = 0;
        this.liquidCost = 0;
        this.containerCost = 0;

    }
    Statistic(int totalCount, int totalCost,
              int averageQueueLengthLoose,int averageQueueLengthLiquid, int averageQueueLengthContainer,
              int averageWaitingTimeLoose, int averageWaitingTimeLiquid, int averageWaitingTimeContainer,
              int maxDeviationLoose, int maxDeviationLiquid, int maxDeviationContainer,
              int averageDeviationLoose, int averageDeviationLiquid, int averageDeviationContainer,
              int containerCost, int looseCost, int liquidCost)
    {
        this.totalCount = totalCount;
        this.totalCost = totalCost;
        this.averageQueueLengthLoose = averageQueueLengthLoose;
        this.averageQueueLengthContainer = averageQueueLengthContainer;
        this.averageQueueLengthLiquid = averageQueueLengthLiquid;
        this.averageWaitingTimeContainer = averageWaitingTimeContainer;
        this.averageWaitingTimeLiquid = averageWaitingTimeLiquid;
        this.averageWaitingTimeLoose = averageWaitingTimeLoose;
        this.maxDeviationLoose = maxDeviationLoose;
        this.maxDeviationLiquid = maxDeviationLiquid;
        this.maxDeviationContainer = maxDeviationContainer;
        this.averageDeviationLoose = averageDeviationLoose;
        this.averageDeviationLiquid = averageDeviationLiquid;
        this.averageDeviationContainer = averageDeviationContainer;
        this.containerCost = containerCost; this.looseCost = looseCost; this.liquidCost = liquidCost;
    }

    private int averageQueueLength(LinkedList<Task> tasker)
    {
        LinkedList<Integer> queue = new LinkedList<>();
        int count = tasker.size();
        int totalLength = 0;
        for ( int i = 0; i < count; i++)
        {
            int num = 0;
            for ( int j = 1; j < count-1; j++)
            {
                if (tasker.get(i).isDone())
                {
                    if(tasker.get(i).getEmptyTime().bigger(tasker.get(j).getUnloadingTimeStart()))
                    {
                        num++;
                    }
                    else{
                        break;
                    }
                }
                else {
                    num++;
                }
            }
            queue.add(num);
        }

        for (Integer num: queue)
        {
            totalLength+=num;
        }
        return totalLength/count;
    }

    private int averageWaitingTime(LinkedList<Task> tasker)
    {
        int maxTime = 0;
        int count = tasker.size();
        for ( int i = 0; i < count; i++)
        {
            if (tasker.get(i).isDone())
            {
                maxTime += tasker.get(i).getDelay();
            }
        }
        return maxTime/count;
    }

    private int maxDeviation(LinkedList<Task> stat)
    {
        int maxDeviation = 0;
        for (Task task: stat)
        {
            int newMax = task.getShip().getDeviationFromTheSchedule().getTimeInSeconds();
            if (newMax > maxDeviation)
            {
                maxDeviation = newMax;
            }
        }
        return maxDeviation;
    }

    private int averageDeviation(LinkedList<Task> tasker)
    {
        int count = tasker.size();
        int deviation = 0;
        for (Task task: tasker)
        {
            deviation += task.getShip().getDeviationFromTheSchedule().getTimeInSeconds();
        }
        return deviation/count;
    }

    private String averageWaitingTime()
    {
        return "\n For LOOSE: " + averageWaitingTimeLoose +
                "\n For LIQUID: " + averageWaitingTimeLiquid +
                "\n For CONTAINER: " + averageWaitingTimeContainer;
    }
    private String maxDeviation()
    {
        return "\n For LOOSE: " +  maxDeviationLoose +
                "\n For LIQUID: " + maxDeviationLiquid +
                "\n For CONTAINER: "+ maxDeviationContainer;
    }

    private String averageDeviation()
    {
        return "\n For LOOSE: " +  averageDeviationLoose +
                "\n For LIQUID: " + averageDeviationLiquid +
                "\n For CONTAINER: "+ averageDeviationContainer;
    }

    private String averageQueueLength()
    {
        return "\n For LOOSE: " + averageQueueLengthLoose +
                "\n For LIQUID: " + averageQueueLengthLiquid +
                "\n For CONTAINER: "+ averageQueueLengthContainer;
    }

    public Statistic collectStatistic(LinkedList<Task> statLoose, LinkedList<Task> statLiquid, LinkedList<Task> statContainer)
    {
        int countOfLooseShips = 0;
        for (Task task: statLoose)
        {
            countOfLooseShips++;
            this.looseCost += task.getExpenses();
            System.out.println(task.toString());
        }
        System.out.println();
        int countOfLiquidShips = 0;
        for (Task task: statLiquid)
        {
            countOfLiquidShips++;
            this.liquidCost += task.getExpenses();
            System.out.println(task.toString());
        }
        System.out.println();
        int countOfContainerShips = 0;
        for (Task task: statContainer)
        {
            countOfContainerShips++;
            this.containerCost += task.getExpenses();
            System.out.println(task.toString());
        }
        System.out.println();
        this.totalCount = countOfContainerShips + countOfLiquidShips + countOfLooseShips;
        this.totalCost = containerCost + looseCost + liquidCost;

        this.averageQueueLengthLoose = averageQueueLength(statLoose);
        this.averageQueueLengthLiquid = averageQueueLength(statLiquid);
        this.averageQueueLengthContainer = averageQueueLength(statContainer);

        this.averageWaitingTimeContainer= averageWaitingTime(statContainer);
        this.averageWaitingTimeLiquid = averageWaitingTime(statLiquid);
        this.averageWaitingTimeLoose = averageWaitingTime(statLoose);

        this.maxDeviationLoose = maxDeviation(statLoose);
        this.maxDeviationLiquid = maxDeviation(statLiquid);
        this.maxDeviationContainer = maxDeviation(statContainer);

        this.averageDeviationLoose = averageDeviation(statLoose);
        this.averageDeviationLiquid = averageDeviation(statLiquid);
        this.averageDeviationContainer = averageDeviation(statContainer);

        return new Statistic(totalCount, totalCost,
                averageQueueLengthLoose, averageQueueLengthContainer, averageQueueLengthLiquid,
                averageWaitingTimeContainer, averageWaitingTimeLiquid, averageWaitingTimeLoose,
                maxDeviationLoose, maxDeviationLiquid, maxDeviationContainer,
                averageDeviationLoose,averageDeviationLiquid, averageDeviationContainer,
                containerCost, looseCost, liquidCost);
    }

    public int getOptimalLooseCount(int count)
    {
        if (looseCost > CRANE_COST)
        {
            count++;
        }
        return count;
    }

    public int getOptimalLiquidCount(int count)
    {
        if (liquidCost> CRANE_COST)
        {
            count++;
        }
        return count;
    }

    public int getOptimalContainerCount(int count)
    {
        if (containerCost > CRANE_COST){
            count++;
        }
        return count;
    }

    public boolean isOptimal()
    {
        return (liquidCost < CRANE_COST && looseCost < CRANE_COST && containerCost < CRANE_COST);
    }

    @Override
    public String toString(){
        return "Число разгруженных судов: " + totalCount +
                "\nСредняя длина очереди на разгрузку: " + averageQueueLength() +
                "\nСреднее время ожидания в очереди: " + averageWaitingTime() +
                "\nМаксимальная задержка разгрузки: " + maxDeviation() +
                "\nСредняя задержка разгрузки: " + averageDeviation() +
                "\nОбщая сумма штрафа: " + totalCost +
                "\nШтраф LOOSE: " + looseCost +
                "\nШтраф LIQUID: " + liquidCost +
                "\nШтраф CONTAINER: " + containerCost;
    }
}
