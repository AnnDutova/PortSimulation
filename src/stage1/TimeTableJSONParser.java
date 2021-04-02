package stage1;

import javafx.util.Pair;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class TimeTableJSONParser
{
    public static final String JSON_FILE_NAME = "timeTable.json";

    public void writeJSON(TimeTable timeTable)throws IOException
    {
        JSONArray ships = new JSONArray();
        for (int index = 0; index < timeTable.getSize(); index++)
        {
            JSONObject shipDetails = new JSONObject();
            shipDetails.put("Name", timeTable.getValueByIndex(index).getName());
            shipDetails.put("CargoType", timeTable.getValueByIndex(index).getCargoTypeString());
            shipDetails.put("CargoWeight", timeTable.getValueByIndex(index).getCargoWeight());
            shipDetails.put("Coming time", timeTable.getValueByIndex(index).getComingTime().toString());
            shipDetails.put("Devastation", timeTable.getValueByIndex(index).getDeviationFromTheSchedule().toString());
            shipDetails.put("Unload wait", timeTable.getValueByIndex(index).getWaitingForUnloading().toString());
            JSONObject obj = new JSONObject();
            obj.put("Time", timeTable.getPairByIndex(index).getKey().toString());
            obj.put("Ship", shipDetails);
            ships.add(obj);
        }
        try(FileWriter file = new FileWriter(JSON_FILE_NAME))
        {
            file.write(ships.toJSONString());
            file.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Task> readJSON()
    {
        ArrayList<Task> tasker = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(JSON_FILE_NAME))
        {
            Object obj = jsonParser.parse(reader);
            JSONArray timeTableList = (JSONArray) obj;
            for (Object cell: timeTableList)
            {
                parseTimeTableObject((JSONObject) cell, tasker );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tasker.sort(new TaskComparator());
        return tasker;
    }

    private void parseTimeTableObject(JSONObject cell, ArrayList tasker)
    {
        String invalidTimeBySchedule = (String) cell.get("Time");
        Time timeBySchedule = parseString(invalidTimeBySchedule);

        JSONObject shipObject = (JSONObject) cell.get("Ship");

        String shipName = (String) shipObject.get("Name");

        Long invalidCargoWeight = (Long) shipObject.get("CargoWeight");
        int cargoWeight = invalidCargoWeight.intValue();

        String invalidType = (String) shipObject.get("CargoType");
        CargoType type = parseCargoType(invalidType);

        String invalidComingTime = (String) shipObject.get("Coming time");
        Time comingTime = parseString(invalidComingTime);

        String invalidDevastation = (String) shipObject.get("Devastation");
        Time devastation = parseString(invalidDevastation);

        timeBySchedule.plus(devastation);

        String invalidUnloadingDevastation = (String) shipObject.get("Unload wait");
        Time unloadingDevastation = parseString(invalidUnloadingDevastation);

        DockedShip ship = new DockedShip(shipName, type, cargoWeight, comingTime, devastation, unloadingDevastation );
        Task task = new Task(ship);
        tasker.add(task);

    }

    private Time parseString(String invalidTime)
    {
        String[] numbers = invalidTime.split(":");
        Time time = new Time();
        try {
            int day = Integer.parseInt(numbers[0]);
            int hour = Integer.parseInt(numbers[1]);
            int minutes = Integer.parseInt(numbers[2]);
            time = new Time(day, hour, minutes);
        }
        catch(InvalidValue ex)
        {
            ex.fillInStackTrace();
        }
        return time;
    }

    private CargoType parseCargoType(String invalid)
    {
        CargoType output = CargoType.LOOSE;
        switch(invalid){
            case "LOOSE":
                output = CargoType.LOOSE;
                break;
            case "LIQUID":
                output =  CargoType.LIQUID;
                break;
            case "CONTAINER":
                output =  CargoType.CONTAINER;
                break;
        }
        return output;
    }

}
class TaskComparator implements Comparator<Task>
{
    @Override
    public int compare(Task o1, Task o2)
    {
        return o1.getComingTime().compareTo(o2.getComingTime());
    }
}
