package stage1.services;

import stage1.schedule.TimeTable;

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
