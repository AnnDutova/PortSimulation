package stage1.threads;

public interface IObservable
{
    void addListener(Crane object);
    void removeListener(Crane object);
    void notifyObservers(int newTime);
}