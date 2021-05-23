package ThreadDispather;

public abstract class ThreadedTask implements Runnable {

    public abstract void start();

    public abstract String getName();

    @Override
    public void run() {
        start();
    }

}
