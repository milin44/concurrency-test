package milin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class Runner {

  // task definitions
  private static CompletableFuture<Integer> blockingTask1(int input) {
    Log.log("called blockingTask1");
    return BlockingTask.request("blockingTask1", 1, input + 1);
  }

  private static Supplier<Integer> supplierBlockingTask1(int input) {
    Log.log("called blockingSupplierTask1");
    return BlockingSupplier.getTask("blockingSupplierTask1", 1, input + 1);
  }

  private static CompletableFuture<Integer> blockingTask2(int input) {
    Log.log("called blockingTask2");
    return BlockingTask.request("blockingTask2", 2, input + 2);
  }

  private static Supplier<Integer> supplierBlockingTask2(int input) {
    Log.log("called blockingSupplierTask2");
    return BlockingSupplier.getTask("blockingSupplierTask2", 2, input + 2);
  }

  private static CompletableFuture<Integer> blockingTask3(int input) {
    Log.log("called blockingTask3");
    return BlockingTask.request("blockingTask3", 3, input + 3);
  }

  private static Supplier<Integer> supplierBlockingTask3(int input) {
    Log.log("called blockingSupplierTask3");
    return BlockingSupplier.getTask("blockingSupplierTask3", 3, input + 3);
  }

  private static CompletableFuture<Integer> blockingTask4(int input) {
    Log.log("called blockingTask4");
    return BlockingTask.request("blockingTask4", 1, input + 4);
  }

  private static Supplier<Integer> supplierBlockingTask4(int input) {
    Log.log("called blockingSupplierTask4");
    return BlockingSupplier.getTask("blockingSupplierTask4", 4, input + 4);
  }


  private static CompletableFuture<Integer> nonBlockingTask1(int input) {
    Log.log("called nonBlockingTask1");
    return NonBlockingTask.request("nonBlockingTask1", 1, input + 1);
  }

  private static CompletableFuture<Integer> nonBlockingTask2(int input) {
    Log.log("called nonBlockingTask2");
    return NonBlockingTask.request("nonBlockingTask2", 2, input + 2);
  }

  private static CompletableFuture<Integer> nonBlockingTask3(int input) {
    Log.log("called nonBlockingTask3");
    return NonBlockingTask.request("nonBlockingTask3", 3, input + 3);
  }

  private static CompletableFuture<Integer> nonBlockingTask4(int input) {
    Log.log("called nonBlockingTask4");
    return NonBlockingTask.request("nonBlockingTask4", 1, input + 4);
  }

  private static CompletableFuture<Integer> runSyncBlocking() {
    return blockingTask1(1).thenCompose(i1 -> {
      Log.log("blockingTask1 finished");
      return blockingTask2(i1).thenCombine(blockingTask3(i1), (i2, i3) -> {
        Log.log("blockingTask2,3 finished");
        return i2 + i3;
      });
    })
        .thenCompose(i4 -> blockingTask4(i4).thenApply(i5 -> {
          Log.log("blockingTask4 finished");
          return i5;
        }));
  }

  private static CompletableFuture<Integer> runAsyncBlocking() {

    final CompletableFuture<Integer> task1 =
        CompletableFuture.supplyAsync(supplierBlockingTask1(1));

    return task1.thenComposeAsync(i1 -> {
      Log.log("blockingTask1 finished");
      return blockingTask2(i1).thenCombineAsync(blockingTask3(i1), (i2, i3) -> {
        Log.log("blockingTask2,3 finished");
        return i2 + i3;
      });
    })
        .thenComposeAsync(i4 -> blockingTask4(i4).thenApply(i5 -> {
          Log.log("blockingTask4 finished");
          return i5;
        }));
  }

  private static CompletableFuture<Integer> runAsyncBlockingWithExecutor() {
    final ExecutorService executor = Executors.newFixedThreadPool(4);


    final CompletableFuture<Integer> task1 =
        CompletableFuture.supplyAsync(supplierBlockingTask1(1));

    return task1.thenComposeAsync(i1 -> {
      Log.log("blockingTask1 finished");
      return blockingTask2(i1).thenCombineAsync(blockingTask3(i1), (i2, i3) -> {
        Log.log("blockingTask2,3 finished");
        return i2 + i3;
      }, executor);
    }, executor)
        .thenComposeAsync(i4 -> blockingTask4(i4).thenApply(i5 -> {
          Log.log("blockingTask4 finished");
          return i5;
        }), executor);
  }

  private static CompletableFuture<Integer> runAsyncBlockingSupplyAsync() {
    final CompletableFuture<Integer> task1 =
        CompletableFuture.supplyAsync(supplierBlockingTask1(1));

    final CompletableFuture<Integer> task2 =
        CompletableFuture.supplyAsync(supplierBlockingTask2(2));

    final CompletableFuture<Integer> task3 =
        CompletableFuture.supplyAsync(supplierBlockingTask3(3));

    final CompletableFuture<Integer> task4 =
        CompletableFuture.supplyAsync(supplierBlockingTask4(4));

    return task1.thenComposeAsync(i1 -> {
      Log.log("nonBlockingTask1 finished");
      return  task2.thenCombineAsync(task3, (i2, i3) -> {
        Log.log("nonBlockingTask1,3 finished");
        return i2 + i3;
      });
    })
        .thenComposeAsync(i4 -> task4.thenApply(i5 -> {
          Log.log("nonBlockingTask4 finished");
          return i5;
        }));
  }

  private static CompletableFuture<Integer> runSyncBlockingSupplyAsync() {
    final CompletableFuture<Integer> task1 =
        CompletableFuture.supplyAsync(supplierBlockingTask1(1));

    final CompletableFuture<Integer> task2 =
        CompletableFuture.supplyAsync(supplierBlockingTask2(2));

    final CompletableFuture<Integer> task3 =
        CompletableFuture.supplyAsync(supplierBlockingTask3(3));

    final CompletableFuture<Integer> task4 =
        CompletableFuture.supplyAsync(supplierBlockingTask4(4));

    return task1.thenCompose(i1 -> {
      Log.log("nonBlockingTask1 finished");
      return  task2.thenCombine(task3, (i2, i3) -> {
        Log.log("nonBlockingTask1,3 finished");
        return i2 + i3;
      });
    })
        .thenCompose(i4 -> task4.thenApply(i5 -> {
          Log.log("nonBlockingTask4 finished");
          return i5;
        }));
  }

  private static CompletableFuture<Integer> runAsyncNonblocking() {
    return nonBlockingTask1(1).thenComposeAsync(i1 -> {
      Log.log("nonBlockingTask1 finished");
      return  nonBlockingTask2(i1).thenCombineAsync(nonBlockingTask3(i1), (i2, i3) -> {
        Log.log("nonBlockingTask1,3 finished");
        return i2 + i3;
      });
    })
        .thenComposeAsync(i4 -> nonBlockingTask4(i4).thenApply(i5 -> {
          Log.log("nonBlockingTask4 finished");
          return i5;
        }));
  }

  private static void timeComplete(Supplier<CompletableFuture<Integer>> test, String name) {
    Log.log("Starting " + name);
    long start = System.currentTimeMillis();
    Integer result = test.get().join();
    long time = System.currentTimeMillis() - start;
    Log.log(name + " returned " + result + " in " + time + " ms.");
    System.out.println();
  }

  public static void main(String[] args) throws Exception {

    // doing things using the thenAsync methods does not imply that things are running in parallell
    // it only means that the result will be handled by another thread

    /*
    timeComplete(Runner::runSyncBlocking, "runSyncBlocking");

    timeComplete(Runner::runAsyncBlocking, "runAsyncBlocking");


    timeComplete(Runner::runAsyncBlockingSupplyAsync, "runAsyncBlockingSupplyAsync");

*/

    timeComplete(Runner::runAsyncBlockingSupplyAsync, "runAsyncBlockingSupplyAsync");

    timeComplete(Runner::runSyncBlockingSupplyAsync, "runSyncBlockingSupplyAsync");

    timeComplete(Runner::runAsyncNonblocking, "runAsyncNonblocking");

    System.exit(1);
  }
}