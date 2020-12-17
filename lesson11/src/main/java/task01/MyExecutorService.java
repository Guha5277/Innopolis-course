package task01;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class MyExecutorService implements Executor {
    private boolean isInterrupted;
    private final Queue<Runnable> tasksQueue;

    /**
     * Создаёт сервси с заданным количеством потоков исполнения
     * @param threadsCount количество потоков
     */
    public MyExecutorService(int threadsCount) {
        tasksQueue = new LinkedBlockingQueue<Runnable>();
        for (int i = 0; i < threadsCount; i++) {
            new Thread(new TaskWorker()).start();
        }
    }

    /**
     * Добавляет задачу в очередь на выполнение
     * @param command выполняемая задача
     */
    public void execute(Runnable command) {
        if (!isInterrupted) {
            tasksQueue.add(command);
        }
    }

    /**
     * Завершает работу сервиса
     */
    public void shutdown(){
        isInterrupted = true;
    }

    /**
     * Внутренний класс реализующий отдельный поток исполнения
     */
    private class TaskWorker implements Runnable {
        public void run() {
            while (!isInterrupted) {
                Runnable task = tasksQueue.poll();
                if (task != null) {
                    task.run();
                }
            }
        }
    }
}
