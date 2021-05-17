package stage1.ships;

import stage1.enums.CargoType;
import stage1.enums.ShipState;
import stage1.time.Time;

public class DockedShip extends AShip
{
    private ShipFactory factory = new ShipFactory();

    private final Time deviationFromTheSchedule;
    private final Time waitingForUnloading;
    private ShipState state;

    public DockedShip(String name, CargoType type, int cargoWeight, Time comingTime)
    {
        super(name, type, cargoWeight, comingTime);
        this.deviationFromTheSchedule = factory.generateShipDevastationFromTheCheadle();
        this.waitingForUnloading = factory.generateShipUnloadDevastation();
        this.state = ShipState.FLOAT;
    }

    public DockedShip(String name, CargoType type, int cargoWeight, Time comingTime,
                      Time deviationFromTheSchedule, Time waitingForUnloading)
    {
        super(name, type, cargoWeight, comingTime);
        this.deviationFromTheSchedule = deviationFromTheSchedule;
        this.waitingForUnloading = waitingForUnloading;
        this.state = ShipState.FLOAT;
    }

    private DockedShip(String name, CargoType type, int cargoWeight, Time comingTime,
                      Time deviationFromTheSchedule, Time waitingForUnloading, ShipState state)
    {
        super(name, type, cargoWeight, comingTime);
        this.deviationFromTheSchedule = deviationFromTheSchedule;
        this.waitingForUnloading = waitingForUnloading;
        this.state = state;
    }

    public Time getDeviationFromTheSchedule()
    {
        return deviationFromTheSchedule;
    }

    public Time getWaitingForUnloading()
    {
        return waitingForUnloading;
    }

    public ShipState getState ()
    {
        return state;
    }

    public void setWaitingState ()
    {
        this.state = ShipState.WAIT;
    }

    public synchronized void takePeaceOfCargo(int weight)
    {
        if (super.getCargoWeight() <= 0)
        {
            state = ShipState.EMPTY;
        }
        else {
            int cargoWeight = super.getCargoWeight();
            cargoWeight -= weight;
            //System.out.println(getFibonacciValue(randomValue()));
            super.setCargoWeight(cargoWeight);
            state = weight>0? ShipState.UNLOAD: ShipState.EMPTY;
        }
    }

    public DockedShip copy()
    {
        return new DockedShip(super.getName(),
                super.getCargoType(),
                super.getCargoWeight(),
                super.getComingTime(),
                this.getDeviationFromTheSchedule(),
                this.getWaitingForUnloading());
    }

    public DockedShip copyForStatistic(int weight)
    {
        return new DockedShip(super.getName(),
                super.getCargoType(),
                weight,
                super.getComingTime(),
                this.getDeviationFromTheSchedule(),
                this.getWaitingForUnloading(),
                this.state);
    }
   //чтобы visualvm красиво показал нашу работу

    public int getFibonacciValue(int n) {
        if (n <= 1) {
            return 0;
        } else if (n == 2) {
            return 1;
        } else  {
            return getFibonacciValue(n - 1) + getFibonacciValue(n - 2);
        }
    }

    public int randomValue(){
        return (int)( Math.random()*20 + 20);
    }

}
