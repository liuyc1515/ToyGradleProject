package toy.learn;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureCancel {

    public FutureCancel() {}

    public void run() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> task = executor.submit(() -> {
            try {
                Thread.sleep(10000);
            }
            catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            return 0;
        });

        task.cancel(true);

        Integer result = -1;
        try {
            result = task.get();
        }
        catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
        catch (ExecutionException e) {
            System.out.println("ExecutionException");
        }
        catch (CancellationException e) {
            System.out.println("CancellationException");
        }
        System.out.println(String.format("Result: %d", result));
        
        return;
    }
}
