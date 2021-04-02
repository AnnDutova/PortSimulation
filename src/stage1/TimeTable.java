package stage1;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public class TimeTable
{
    private ArrayList<Pair<Time, DockedShip>> timeTable = new ArrayList<Pair<Time, DockedShip>>();
    private ShipFabrics fabrics = new ShipFabrics();

    public final int MAX_OF_SHIPS = 100;

    TimeTable() {}

    TimeTable(LinkedList<DockedShip> board)
    {
        fillTimeTable(board);
    }

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

    public void sortTimeTable()
    {
        timeTable.sort(new DataComparator());
    }

}

class DataComparator implements Comparator<Pair<Time, DockedShip>>
{
    @Override
    public int compare(Pair<Time, DockedShip> o1, Pair<Time, DockedShip> o2)
    {
        return o1.getKey().compareTo(o2.getKey());
    }
}
