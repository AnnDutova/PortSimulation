package stage1.schedule;

import javafx.util.Pair;
import stage1.comparators.DataComparator;
import stage1.ships.DockedShip;
import stage1.ships.ShipFactory;
import stage1.time.Time;

import java.util.ArrayList;
import java.util.LinkedList;

public class TimeTable
{
    private ArrayList<Pair<Time, DockedShip>> timeTable = new ArrayList<>();
    private ShipFactory fabrics = new ShipFactory();
    public final int MAX_OF_SHIPS = 150;


    public TimeTable() {}

    public void generateTimeTable()
    {
        fillTimeTable(fabrics.generateShipList(MAX_OF_SHIPS));
    }

    public void print()
    {
        for (Pair pair: timeTable)
        {
            System.out.println( pair.getKey().toString());
            System.out.println( pair.getValue().toString());
            System.out.println();
        }
    }

    public Pair getPairByIndex(int index)
    {
        return timeTable.get(index);
    }

    public DockedShip getValueByIndex(int index)
    {
        return timeTable.get(index).getValue();
    }

    public Time getKeyByIndex(int index)
    {
        return timeTable.get(index).getKey();
    }

    public int getSize()
    {
        return timeTable.size();
    }

    public void addCellWithFixedTime(Pair<Time, DockedShip> cell)
    {
        timeTable.add(cell);
    }

    public void addShip(DockedShip ship)
    {
        Pair<Time, DockedShip> cell = new Pair<>(ship.getComingTime(), ship);
        timeTable.add(cell);
        timeTable.sort(new DataComparator());
    }

    private void fillTimeTable(LinkedList<DockedShip> ships)
    {
        for (DockedShip ship: ships)
        {
            timeTable.add(new Pair(ship.getComingTime(), ship));
        }
        timeTable.sort(new DataComparator());
    }

}

