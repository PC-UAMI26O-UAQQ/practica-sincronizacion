import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SumaLock implements Runnable {

    private static final int ITERACIONES = 50_000;
    private static final int HILOS = 4;

    private static int x = 0;
    private static final Lock CANDADO = new ReentrantLock();

    @Override
    public void run() {
        for (int i = 0; i < ITERACIONES; i++) {
            CANDADO.lock();

            x++;

            CANDADO.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] trabajadores = new Thread[HILOS];

        for (int i = 0; i < HILOS; i++) {
            trabajadores[i] = new Thread(new SumaLock(), "T" + i);
            trabajadores[i].start();
        }

        for (Thread trabajador : trabajadores) {
            trabajador.join();
        }

        System.out.println("Valor esperado: " + (HILOS * ITERACIONES));
        System.out.println("Valor obtenido: " + x);
    }
}
