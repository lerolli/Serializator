package ThreadDispather;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class ThreadDispatcher {

    private static volatile ThreadDispatcher instance;

    public final ThreadMonitor threadMonitor = new ThreadMonitor();
    public GlobalQueue queue = GlobalQueue.getInstance();
    public final ArrayList<ThreadedTask> threadedTask = new ArrayList<>();
    private ThreadWorker[] threadWorkers;

    public ThreadDispatcher(){
        queue = GlobalQueue.getInstance();
        add(threadMonitor);
    }

    public static ThreadDispatcher getInstance() {
        if (instance == null) {
            synchronized (ThreadDispatcher.class) {
                if (instance == null) {
                    instance = new ThreadDispatcher();
                }
            }
        }
        return instance;
    }

    public ThreadedTask getTaskFromGlobalQueue() {
        synchronized (queue) {
            return queue.remove();
        }
    }

    public void add(ThreadedTask task) {
        synchronized (threadedTask) {
            threadedTask.add(task);
        }
        new Thread(task).start();
        //threadMonitor.ThreadStart(task);
    }

    public void addInQueue(ThreadedTask task){
        synchronized (threadedTask) {
            threadedTask.add(task);
        }

        synchronized (ThreadDispatcher.GlobalQueue.getInstance()) {
            queue.add(task);
        }
        synchronized (threadMonitor) {
            threadMonitor.update();
        }
    }

    public void setMaxPoolSize(int maxSize){
        threadWorkers = new ThreadWorker[maxSize];
        for (int i = 0; i < maxSize; i++){
            threadWorkers[i] = new ThreadWorker();
        }
        for (var i : threadWorkers){
            var thread = new Thread(i);
            thread.start();
        }
    }

    public static class GlobalQueue
    {
        private static GlobalQueue instance;
        private static ArrayDeque<ThreadedTask> globalQueue;

        protected GlobalQueue() {
            globalQueue = new ArrayDeque<>();
        }

        public static GlobalQueue getInstance() {
            if (instance == null) {
                instance = new GlobalQueue();
            }
            return instance;
        }

        public void add(ThreadedTask threadedTask) {
            globalQueue.add(threadedTask);
        }

        public ThreadedTask remove() {
            if (globalQueue.size() != 0)
                return globalQueue.remove();
            else
                return null;
        }

        public int Size(){
            return globalQueue.size();
        }

        public ArrayDeque<ThreadedTask> GetQueue(){
            if (globalQueue == null)
                return new ArrayDeque<>();
            return globalQueue;
        }
    }
}