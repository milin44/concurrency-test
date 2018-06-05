package milin;

import java.util.Timer;
import java.util.function.Supplier;

public class BlockingSupplier {
    private static final Timer timer = new Timer();


  public static  <T> Supplier<T> getTask(final String name, int delay, T value) {
    return () -> {
      Log.log(String.format("request: %s starting ", name));
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