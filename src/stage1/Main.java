package stage1;

import stage1.services.ServiceOne;
import stage1.services.ServiceThree;
import stage1.services.ServiceTwo;

import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {
        ServiceOne one = new ServiceOne();
        one.start();
        ServiceTwo two = new ServiceTwo(one.giveSchedule());
        two.start();
        Semaphore mutex = new Semaphore(1);
        ServiceThree three = new ServiceThree(mutex);
        three.start();

        try
        {
            mutex.acquire();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        three.printStatistic();
        mutex.release();
    }
}
