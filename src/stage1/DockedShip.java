package stage1;

public class DockedShip extends AShip
{
    private ShipFabrics fabrics = new ShipFabrics();
    private Time deviationFromTheSchedule;
    private Time waitingForUnloading;
    //для потоков
    private ShipState state;

    public DockedShip(String name, CargoType type, int cargoWeight, Time comingTime)
    {
        super(name, type, cargoWeight, comingTime);
        this.deviationFromTheSchedule = fabrics.generateShipDevastationFromTheCheadle();
        this.waitingForUnloading = fabrics.generateShipUnloadDevastation();
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

    public synchronized void takePeaceOfCargo(int weight)
    {
        if (cargoWeight <= 0)
        {
            state = ShipState.EMPTY;
        }
        else {
        super.cargoWeight-= weight;
        state = weight>0? ShipState.UNLOAD: ShipState.EMPTY;
        }
    }
    public DockedShip copy()
    {
        DockedShip newShip = new DockedShip(getName(), getCargoType(), getCargoWeight(), getComingTime(),
                getDeviationFromTheSchedule(), getWaitingForUnloading());
        return newShip;
    }

}
