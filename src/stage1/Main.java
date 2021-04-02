package stage1;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

public class Main
{
    public static void main(String[] args)
    {
        ServiceOne one = new ServiceOne();
        one.start();
        ServiceTwo two = new ServiceTwo(one.giveSchedule());
        two.start();
        Mutex mutex = new Mutex();
        ServiceThree three = new ServiceThree(mutex);
        three.start();

        try
        {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        three.printStatistic();
        mutex.release();
    }
}
