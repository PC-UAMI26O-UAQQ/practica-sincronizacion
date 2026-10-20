import java.util.concurrent.Semaphore;

public class SumaSemaforo implements Runnable {

    private static final int ITERACIONES = 50_000;
    private static final int HILOS = 4;

    private static int x = 0;
    private static final Semaphore SEMAFORO = new Semaphore(1);

    @Override
    public void run() {
        for (int i = 0; i < ITERACIONES; i++) {
            try {
                SEMAFORO.acquire();

                x++;

                SEMAFORO.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] trabajadores = new Thread[HILOS];

        for (int i = 0; i < HILOS; i++) {
            trabajadores[i] = new Thread(new SumaSemaforo(), "T" + i);
            trabajadores[i].start();
        }

        for (Thread trabajador : trabajadores) {
            trabajador.join();
        }

        System.out.println("Valor esperado: " + (HILOS * ITERACIONES));
        System.out.println("Valor obtenido: " + x);
    }
}
