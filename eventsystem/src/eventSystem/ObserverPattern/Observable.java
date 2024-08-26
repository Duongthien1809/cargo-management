package eventSystem.ObserverPattern;


public interface Observable {
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void observerNotify(String status);
}
