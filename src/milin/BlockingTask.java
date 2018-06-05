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
    future.complete(getTask(name, delay, value).get());

    return future;
  }

  private static <T> Supplier<T> getTask(final String name, int delay, T value) {
    return () -> { // get
      try {
        Thread.sleep(delay * 1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Log.log(String.format("request: %s finished ", name));
      return value;
    };
  }

}