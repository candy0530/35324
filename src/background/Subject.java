package background;

public interface Subject {
    public void registOberserver(Observer observer);

    public void removeOberserver(Observer observer);

    public void notifyAllOberserver();
}
