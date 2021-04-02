package stage1;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static stage1.TimeTableJSONParser.JSON_FILE_NAME;

public class ServiceTwo
{
    private TimeTable timeTable;
    
    ServiceTwo(TimeTable timeTable)
    {
        this.timeTable = timeTable;
    }

    public void start()
    {
        addShipManually();
        try
        {
        TimeTableJSONParser parser = new TimeTableJSONParser();
        parser.writeJSON(timeTable);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        System.out.println("Service Two create file: "+ JSON_FILE_NAME);
    }

    private void addShipManually()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            boolean write = true;
            while (write)
            {
                System.out.println("Enter user data");
                System.out.println("Continue?(Y/N)");
                String answer = reader.readLine();
                if (answer.equals("Y")) {
                    System.out.print("Ship name: ");
                    String name = reader.readLine();
                    System.out.print("Ship cargo type(lo/li/co): ");
                    String sType = reader.readLine();
                    while (!sType.equals("lo") && !sType.equals("li") && !sType.equals("co")) {
                        System.out.print("This type doesn't exist. Repeat please.");
                        System.out.print("Ship cargo type(lo/li/co): ");
                        sType = reader.readLine();
                    }
                    CargoType type = parseCargoType(sType);
                    System.out.println("Ship cargo type: "+ type.toString());

                    System.out.print("Cargo weight: ");
                    int weight = -1;
                    while (weight < 0) {
                        try {
                            String sWeight = reader.readLine();
                            weight = Integer.parseInt(sWeight);
                        } catch (Exception ex) {
                            System.out.print("This variable must have int type. Repeat please.");
                            System.out.print("Cargo weight: ");
                            weight = -1;
                        }
                    }

                    System.out.println("Coming time:");

                    int day = -1;
                    while (day < 0) {
                        try {
                            System.out.print("\tEnter day: ");
                            String sDay = reader.readLine();
                            day = Integer.parseInt(sDay);
                            if (day > 29 || day < 0) {
                                System.out.print("Invalid day. Must be < 30 and >0. Repeat please.");
                                day = -1;
                            }
                        } catch (Exception ex) {
                            System.out.print("Invalid day. Repeat please.");
                            day = -1;
                        }
                    }

                    int hour = -1;
                    while (hour < 0)
                    {
                        try
                        {
                            System.out.print("\tEnter hour: ");
                            String sHour = reader.readLine();
                            hour = Integer.parseInt(sHour);
                            if (hour > 24 || hour < 0)
                            {
                                System.out.print("Invalid hour. Must be < 24 and >0. Repeat please.");
                                hour = -1;
                            }
                        } catch (Exception ex) {
                            System.out.print("Invalid hour. Repeat please.");
                            hour = -1;
                        }
                    }

                    int minutes = -1;
                    while(minutes < 0)
                    {
                        try
                            {
                            System.out.print("\tEnter minutes: ");
                            String sMinutes = reader.readLine();
                            minutes = Integer.parseInt(sMinutes);
                            if (minutes > 59 || minutes < 0)
                            {
                                System.out.print("Invalid hour. Must be < 59 and > 0. Repeat please.");
                                minutes = -1;
                            }
                        }
                        catch (Exception ex){
                            System.out.print("Invalid minutes. Repeat please.");
                            minutes = -1;
                        }
                    }

                    Time time = new Time();
                    try {
                        time = new Time(day, hour, minutes);
                    } catch (InvalidValue ex) {
                        ex.fillInStackTrace();
                    }
                    DockedShip ship = new DockedShip(name, type, weight, time);
                    timeTable.addShip(ship);
                } else {
                    write = false;
                }
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private CargoType parseCargoType(String invalid)
    {
        CargoType output = CargoType.LOOSE;
        switch(invalid){
            case "lo":
                output = CargoType.LOOSE;
                break;
            case "li":
                output =  CargoType.LIQUID;
                break;
            case "co":
                output =  CargoType.CONTAINER;
                break;
        }
        return output;
    }

}
