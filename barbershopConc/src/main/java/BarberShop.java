import java.util.concurrent.*;

public class BarberShop {

    private final BlockingQueue<Customer> queue;

    private final BlockingQueue<Boolean> syncQueue;

    public BarberShop(int maxCustomers) {
        this.queue = new LinkedBlockingQueue<>(maxCustomers);
        this.syncQueue = new SynchronousQueue<>();
    }

    public void openShop() {
        // Barber starts working
        Barber barber = new Barber(this);
        new Thread(barber).start();

        // Customers start to arrive
        for (int i = 0; ; i++) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(0, 100));
                new Thread(new Customer(i,this)).start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void enterShop(Customer cust) throws InterruptedException {
        queue.put(cust);
        syncQueue.take();
    }

    public Customer nextCustomer() throws InterruptedException {
        try {
            return queue.take();
        } finally {
            syncQueue.put(true);
        }
    }

    public static void main(String[] args) {
        int maxCustomers = Integer.valueOf(args[0]);

        BarberShop barberShop = new BarberShop(maxCustomers);
        barberShop.openShop();
    }
}
