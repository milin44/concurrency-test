package milin;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BlockingTask {

  /**
   * Build a future to return the value after a delay.
   *
   * @return future
   */
  public static <T> CompletableFuture<T> request(String name, int delay, T value) {
    Log.log(String.format("request: %s starting ", name));
    CompletableFuture<T> future = new CompletableFuture<T>();
    try {
      Thread.sleep(delay * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    future.complete(getTask(name, value).get());

    return future;
  }

  private static <T> Supplier<T> getTask(final String name, T value) {
    return () -> {
      Log.log(String.format("request: %s finished ", name));
      return value;
    };
  }

}