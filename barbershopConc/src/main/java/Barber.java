import java.util.concurrent.ThreadLocalRandom;

public class Barber implements Runnable {

    private BarberShop barberShop;

    public Barber(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    private void cutHair(Customer customer) throws InterruptedException {
        System.out.printf("Barber is cutting Customer %s hair\n", customer.id);
        Thread.sleep(ThreadLocalRandom.current().nextInt(0, 100));
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Barber is sleeping");
                Customer customer = barberShop.nextCustomer();

                cutHair(customer);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
