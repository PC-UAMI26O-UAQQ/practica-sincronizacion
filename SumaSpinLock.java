import java.util.concurrent.locks.ReentrantLock;

public class SumaSpinLock implements Runnable {

    private static final int ITERACIONES = 50_000;
    private static final int HILOS = 4;

    private static int x = 0;
    private static final ReentrantLock CANDADO = new ReentrantLock();

    @Override
    public void run() {
        for (int i = 0; i < ITERACIONES; i++) {

            while (!CANDADO.tryLock()) {
                Thread.onSpinWait();
            }

            x++;

            CANDADO.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] trabajadores = new Thread[HILOS];

        for (int i = 0; i < HILOS; i++) {
            trabajadores[i] = new Thread(new SumaSpinLock(), "T" + i);
            trabajadores[i].start();
        }

        for (Thread trabajador : trabajadores) {
            trabajador.join();
        }

        System.out.println("Valor esperado: " + (HILOS * ITERACIONES));
        System.out.println("Valor obtenido: " + x);
    }
}
