package core.observer;

import java.util.HashMap;
import java.util.Map;

/**
 * AbstractSubject
 *
 * @author gnl
 * @since 2023/2/14
 */
public abstract class Subject {
    private Observer observer;
    private Map<Integer, Observer> observers;

    public Subject() {
        if (observers == null) {
            synchronized (this) {
                if (observers == null) {
                    observers = new HashMap<>();
                }
            }
        }
    }

    public void registerObserver(Observer obs) {
        observers.put(obs.hashCode(), obs);
    }

    public void unregisterObserver(int obsId) {
        observers.remove(obsId);
    }

    public Observer getObserver(int obsId) {
        return observers.get(obsId);
    }

    public Map<Integer, Observer> getObservers() {
        return observers;
    }

    public void notifyObserver(int observerId) {
        Observer obs;
        if ((obs = observers.get(observerId)) !=  null) {
            obs.update();
        }
    }

    public void notifyAllObservers() {
        observers.forEach((id, obs) -> obs.update());
    }

    abstract void doSomething();

}
