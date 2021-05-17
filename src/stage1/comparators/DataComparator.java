package stage1.comparators;

import javafx.util.Pair;
import stage1.ships.DockedShip;
import stage1.time.Time;

import java.util.Comparator;

public class DataComparator implements Comparator<Pair<Time, DockedShip>> {
    @Override
    public int compare(Pair<Time, DockedShip> o1, Pair<Time, DockedShip> o2) {
        return o1.getKey().compareTo(o2.getKey());
    }
}