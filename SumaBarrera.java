import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

public class SumaBarrera implements Runnable {

    private static final int ITERACIONES = 50_000;
    private static final int HILOS = 4;

    private static int x = 0;
    private static final ReentrantLock CANDADO = new ReentrantLock();

    private static final CyclicBarrier BARRERA = new CyclicBarrier(
        HILOS,
        () -> System.out.println(
            "Todos llegaron a la barrera. X = " + x
        )
    );

    @Override
    public void run() {
        for (int i = 0; i < ITERACIONES; i++) {
            CANDADO.lock();

            x++;

            CANDADO.unlock();
        }

        System.out.println(
            Thread.currentThread().getName() + " llego a la barrera"
        );

        try {
            BARRERA.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        } catch (BrokenBarrierException e) {
            System.out.println("La barrera fue rota");
            return;
        }

        System.out.println(
            Thread.currentThread().getName() + " paso la barrera"
        );
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] trabajadores = new Thread[HILOS];

        for (int i = 0; i < HILOS; i++) {
            trabajadores[i] = new Thread(new SumaBarrera(), "T" + i);
            trabajadores[i].start();
        }

        for (Thread trabajador : trabajadores) {
            trabajador.join();
        }

        System.out.println("Valor esperado: " + (HILOS * ITERACIONES));
        System.out.println("Valor final: " + x);
    }
}
