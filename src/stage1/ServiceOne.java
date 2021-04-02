package stage1;

public class ServiceOne
{
    private TimeTable timeTable = new TimeTable();

    public void start()
    {
        timeTable.generateTimeTable();
        timeTable.print();
    }
    public TimeTable giveSchedule()
    {
        return this.timeTable;
    }
}
