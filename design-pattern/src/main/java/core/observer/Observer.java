package core.observer;

/**
 * Abstract Observer
 *
 * @since 2023/2/14
 * @author gnl
 */
public abstract class Observer {

    private int id;

    public Observer() {
        this.id = hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    abstract void update();

}
