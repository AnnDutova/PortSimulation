package stage1;


import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

public class Timer implements Runnable
{
    volatile private int time;
    volatile private boolean stop;

    Timer()
    {
        this.stop = false;
        this.time = 0;
    }

    @Override
    public void run(){
        while(!isStop())
        {
            time++;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " Timer are murdered");
    }

    public boolean isStop()
    {
        return stop;
    }

    public void stop()
    {
        stop = true;
    }

    synchronized public Time getTimerTime() throws InvalidValue
    {
        Time newTime = new Time();
        newTime = newTime.convertSecondsToTime(time);
        return newTime;
    }
}
