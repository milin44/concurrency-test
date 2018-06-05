package milin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class NonBlockingTask {
    private static ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(4);
    
    /**
     * Build a future to return the value after a delay.
     * 
     * @param delay
     * @param value
     * @return future
     */
    public static <T> CompletableFuture<T> request(String name, int delay, T value) {
        CompletableFuture<T> future = new CompletableFuture<T>();
        Runnable task = () -> future.complete(getTask(name, value).get());
        Log.log(String.format("request: %s starting ", name));
        scheduledPool.schedule(task, delay, TimeUnit.SECONDS);
        return future;
    }

    private static  <T> Supplier<T> getTask(final String name, T value) {
        return () -> {
            Log.log(String.format("request: %s finished ", name));
            return value;
        };
    }
}