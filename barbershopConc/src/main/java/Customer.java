import java.util.concurrent.ThreadLocalRandom;

public class Customer implements Runnable {

    int id;

    private BarberShop barberShop;

    public Customer(int id, BarberShop barberShop) {
        this.id = id;
        this.barberShop = barberShop;
    }

    private void getHairCut() throws InterruptedException {
        System.out.printf("Customer %d is having his hair cut\n", id);
        Thread.sleep(ThreadLocalRandom.current().nextInt(0, 100));
    }

    @Override
    public void run() {
        try {
            System.out.printf("Customer %d arrived\n", id);
            barberShop.enterShop(this);

            getHairCut();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
