import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Customer implements Runnable {

    int id;
    private Semaphore semaphore = new Semaphore(0);

    private BarberShop barberShop;

    public Customer(int id, BarberShop barberShop) {
        this.id = id;
        this.barberShop = barberShop;
    }

    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }

    private void getHairCut() throws InterruptedException {
        System.out.printf("Customer %d is having his hair cut\n", id);
        Thread.sleep(ThreadLocalRandom.current().nextInt(0, 100));
    }

    @Override
    public void run() {
        try {
            System.out.printf("Customer %d arrived\n", id);
            if (!barberShop.enterShop(this)) {
                System.out.printf("Customer %d is leaving because the BarberShop is full\n", id);
                return;
            }

            getHairCut();

            barberShop.leaveShop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
