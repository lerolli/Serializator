package ThreadDispather;

import java.util.ArrayDeque;

public class ThreadMonitor extends ThreadedTask {
    private ArrayDeque<ThreadedTask> runningThread;
    private ArrayDeque<ThreadedTask> threadInGlobalQueue;
    public ThreadMonitor() {
        runningThread = new ArrayDeque<>();
        threadInGlobalQueue = ThreadDispatcher.GlobalQueue.getInstance().GetQueue();
    }

    public void start() {
        System.out.println("Running \n" + ArrayDequeToString(runningThread) + '\n' +
                            "Waiting \n" + ArrayDequeToString(threadInGlobalQueue));
    }

    private String ArrayDequeToString(ArrayDeque<ThreadedTask> arrayList){
        var strBuilder = new StringBuilder();
            for (var thread : arrayList) {
                strBuilder.append(thread.getName()).append("\n");
            }
            return strBuilder.toString();
    }

    public void update(){
        start();
    }

    public void RemoveRunningThread(ThreadedTask threadedTask){
        runningThread.remove(threadedTask);
        update();
    }
    @Override
    public String getName() {
        return "ThreadMonitor";
    }

    public void ThreadStart(ThreadedTask thread){
        runningThread.add(thread);
        update();
    }

}
