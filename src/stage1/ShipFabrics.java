package stage1;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

import java.util.LinkedList;

public class ShipFabrics
{
    public LinkedList<DockedShip> generateShipList(int maxCountOfShips)
    {
        int number = (int)(Math.random()*(maxCountOfShips-1) + 1);
        LinkedList<DockedShip> ships = new LinkedList<>();
        for (Integer index = 0; index<number; index++)
        {
            DockedShip newShip = new DockedShip(index.toString(),
                    generateCargoType(),
                    generateWeightInRange(4000, 100),
                    generateRandomDate());
            ships.add(newShip);
        }
        return ships;
    }

    public Time generateShipDevastationFromTheCheadle()
    {
        Time time = new Time();
        int randomDay = 0, randomHour = 0, randomMinutes = 0;
        if (generateDecision())
        {
            randomDay = (int)( Math.random()*7);
            randomHour = generateRandom24number();
            randomMinutes = generateRandom60number();
        }
        try
        {
            time =  new Time (randomDay, randomHour, randomMinutes);
        }
        catch (InvalidValue e)
        {
            e.fillInStackTrace();
        }
        return time;
    }

    public Time generateShipUnloadDevastation()
    {
        Time time = new Time();
        int randomHour = 0, randomMinutes = 0;
        if (generateDecision())
        {
            randomHour = generateRandom24number();
            randomMinutes = generateRandom60number();
        }
        try
        {
            time = new Time(0, randomHour, randomMinutes);
        }
        catch (InvalidValue e)
        {
            e.fillInStackTrace();
        }
        return time;
    }


    private boolean generateDecision()
    {
        int num = (int)(Math.random()*2);
        return num == 0? false: true;
    }

    private CargoType generateCargoType()
    {
        int num = (int)(Math.random()*3);
        CargoType output = CargoType.LOOSE;
        switch (num)
        {
            case 0:
                output = CargoType.LOOSE;
                break;
            case 1:
                output =  CargoType.LIQUID;
                break;
            case 2:
                output =  CargoType.CONTAINER;
                break;
        }
        return output;
    }

    private int generateWeightInRange(int max, int min)
    {
        return (int)((Math.random()*(max-min)) + min);
    }

    private Time generateRandomDate()
    {
        int randomDay = (int)( Math.random()*29 + 1);
        Time newDay = new Time();
        try{
            newDay = new Time(randomDay, generateRandom24number(), generateRandom60number());
        }
        catch(InvalidValue ex)
        {
            ex.fillInStackTrace();
        }
        return newDay;
    }

    private int generateRandom60number()
    {
        int randomNumber = (int)( Math.random()*60-1);
        return randomNumber;
    }

    private int generateRandom24number()
    {
        int randomNumber = (int)( Math.random()*24-1);
        return randomNumber;
    }
}
