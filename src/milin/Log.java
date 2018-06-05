package milin;

public class Log {
  public static void log(String msg) {

    System.out.println(String.format("[%d] %s: %s", System.currentTimeMillis(), Thread.currentThread().getName(), msg));
  }

}
