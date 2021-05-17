package stage1.ships;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

import stage1.enums.CargoType;
import stage1.time.Time;
import static stage1.threads.Crane.*;

abstract public class AShip
{
    private String name;
    private CargoType type;
    private int cargoWeight;
    private Time comingTime;
    private Time parkingTime;

    public AShip(String name, CargoType type, int cargoWeight, Time comingTime)
    {
        this.name = name;
        this.type = type;
        this.cargoWeight = cargoWeight;
        this.comingTime = comingTime;
        parkingTime = setParkingTime();
    }

    private Time setParkingTime()
    {
        int seconds = 0;
        switch(type)
        {
            case LOOSE:
                seconds = (cargoWeight / MAX_WEIGHT_LOOSE_CRANE) * WAIT_FOR_LOOSE;
                break;
            case LIQUID:
                seconds = (cargoWeight / MAX_WEIGHT_LIQUID_CRANE) * WAIT_FOR_LIQUID;
                break;
            case CONTAINER:
                seconds = (cargoWeight / MAX_WEIGHT_CONTAINER_CRANE ) * WAIT_FOR_CONTAINER;
                break;
        }
        Time time = new Time();
        try {
            return time.convertSecondsToTime(seconds);
        } catch (InvalidValue invalidValue) {
            invalidValue.printStackTrace();
        }
        return null;
    }

    public String getName()
    {
        return name;
    }

    public CargoType getCargoType()
    {
        return type;
    }

    public String getCargoTypeString()
    {
        String output = null;
        switch(type){
            case LOOSE:
                output = CargoType.LOOSE.toString();
                break;
            case LIQUID:
                output =  CargoType.LIQUID.toString();
                break;
            case CONTAINER:
                output =  CargoType.CONTAINER.toString();
                break;
        }
        return output;
    }
    public int getCargoWeight()
    {
        return cargoWeight;
    }

    public void setCargoWeight(int weight)
    {
        cargoWeight = weight;
    }

    public Time getComingTime()
    {
        return comingTime;
    }

    public Time getParkingTime()
    {
        return parkingTime;
    }

    @Override
    public String toString()
    {
        return "\nName: " + name +
                "\nCargoType: " + type +
                "\nWeight: " + cargoWeight +
                "\nComing time: " + comingTime +
                "\nParking time: " + parkingTime;
    }

}
