package stage1.time;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

public class Time implements Comparable<Time>
{
    public final static int MAX_DAYS = 30;
    public final static int MAX_HOURS = 24;
    public final static int MAX_MINUTES = 60;

    private int day;
    private int hour;
    private int minutes;

    public Time()
    {
        this.day = 0;
        this.hour = 0;
        this.minutes = 0;
    }

    public Time(int day, int hour, int minutes) throws InvalidValue
    {
        if (day <= MAX_DAYS && day >= 0 )
        {
            this.day = day;
        }
        else throw new InvalidValue("Only 30 days in a month");
        if (hour < MAX_HOURS && hour >= 0)
        {
            this.hour = hour;
        }
        else throw new InvalidValue("Only 24 hours in a day");
        if (minutes < MAX_MINUTES && minutes >= 0)
        {
            this.minutes = minutes;
        }
        else throw new InvalidValue("Only 60 minutes in an hour");
    }

    public int getDay()
    {
        return day;
    }

    public int getHour()
    {
        return hour;
    }

    public int getTimeInSeconds()
    {
        return minutes + hour * MAX_MINUTES  + day * MAX_HOURS * MAX_MINUTES;
    }

    public Time convertSecondsToTime (int milliseconds) throws InvalidValue {
        int days = milliseconds / MAX_MINUTES / MAX_HOURS;
        int hours = (milliseconds - days * MAX_MINUTES * MAX_HOURS) / MAX_MINUTES;
        int minutes = milliseconds - days * MAX_MINUTES * MAX_HOURS - hours * MAX_MINUTES;
        return new Time(days, hours, minutes);
    }

    public Time getDifference(Time time2)
    {
        Time time = new Time();
        try
        {
            time = new Time(Math.abs(day- time2.day), Math.abs(hour - time2.hour), Math.abs(minutes - time2.minutes));
        }
        catch(InvalidValue ex)
        {
            ex.fillInStackTrace();
        }
        return time;
    }

    public Time plus(Time time2)
    {
        int resultDay = day + time2.day;
        int resultHour, resultMinutes;

        resultHour = hour + time2.hour;
        resultMinutes = minutes + time2.minutes;
        if (resultMinutes >= MAX_MINUTES)
        {
            resultHour++;
            resultMinutes -= MAX_MINUTES;
        }
        if (resultHour >= MAX_HOURS)
        {
            resultDay++;
            resultHour -= MAX_HOURS;
        }

        if (resultDay > MAX_DAYS)
        {
            resultDay = MAX_DAYS;
        }

        Time time = new Time();
        try
        {
            time = new Time(resultDay, resultHour, resultMinutes);
        }
        catch (InvalidValue e)
        {
            e.fillInStackTrace();
        }
        return time;
    }

    public Time plus(int wait) throws InvalidValue
    {
        int newMinutes = minutes + wait;
        int newHour = hour;
        int newDay = day;
        while (newMinutes >= MAX_MINUTES)
        {
            newHour++;
            newMinutes-=MAX_MINUTES;
        }
        while (newHour >= MAX_HOURS){
            newDay++;
            newHour -=MAX_HOURS;
        }
        return new Time(newDay, newHour, newMinutes);
    }

    public Time minus(Time time2)
    {
        int resultDay = day - time2.day;
        int resultHour = hour - time2.hour;
        int resultMinutes = minutes - time2.minutes;

        if (resultMinutes < 0)
        {
            resultHour--;
            resultMinutes += MAX_MINUTES;
        }
        if (resultHour < 0)
        {
            resultDay--;
            resultHour += MAX_HOURS;
        }
        if (resultDay < 0)
        {
            resultDay = 0;
        }

        Time time = new Time();
        try
        {
            time = new Time(resultDay, resultHour, resultMinutes);
        }
        catch (InvalidValue e)
        {
            e.fillInStackTrace();
        }
        return time;
    }

    public boolean bigger (Time time2) //time1 more if true
    {
        return getTimeInSeconds() > time2.getTimeInSeconds();
    }

    @Override
    public String toString()
    {
        return day + ":" + hour + ":" + minutes;
    }

    @Override
    public int compareTo(Time time2)
    {
        if (day == time2.day)
        {
            if (hour == time2.hour)
            {
                return Integer.compare(minutes, time2.minutes);
            }
            else if (hour > time2.hour)
            {
                return 1;
            }
            else return -1;
        }
        else if(day > time2.day)
        {
            return 1;
        }
        else return -1;
    }
}
