package ThreadDispather;

public class ThreadWorker implements Runnable {

    @Override
    public void run() {
        var th = new Thread();
        while (true){
            var threadedTask = ThreadDispatcher.getInstance().getTaskFromGlobalQueue();
            if (threadedTask != null){
                ThreadDispatcher.getInstance().threadMonitor.ThreadStart(threadedTask);
                threadedTask.run();

            }
            try {
                Thread.sleep(1000);
                th.join();
                synchronized (ThreadDispatcher.getInstance().threadMonitor) {
                    ThreadDispatcher.getInstance().threadMonitor.RemoveRunningThread(threadedTask);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
