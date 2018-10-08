import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class BarberShop {

    private final int maxCustomers;

    private int numCustomers = 0;
    private final Semaphore mutex = new Semaphore(1);

    private final Semaphore customer = new Semaphore(0);

    private final Semaphore customerDone = new Semaphore(0);
    private final Semaphore barberDone = new Semaphore(0);

    private final Queue<Customer> queue;

    public BarberShop(int maxCustomers) {
        this.maxCustomers = maxCustomers;
        queue = new LinkedList<>();
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

    public boolean enterShop(Customer cust) throws InterruptedException {
        mutex.acquire();
        if (numCustomers == maxCustomers) {
            mutex.release();
            return false;
        }
        numCustomers++;
        queue.add(cust);
        mutex.release();

        customer.release();
        cust.acquire();

        return true;
    }

    public void leaveShop() throws InterruptedException {
        customerDone.release();
        barberDone.acquire();

        mutex.acquire();
        numCustomers--;
        mutex.release();
    }

    public Customer nextCustomer() throws InterruptedException {
        Customer cust = null;
        try {
            customer.acquire();

            mutex.acquire();
            cust = queue.poll();
            mutex.release();

            return cust;
        } finally {
            if (cust != null) {
                cust.release();
            }
        }
    }

    public void releaseCustomer() throws InterruptedException {
        customerDone.acquire();
        barberDone.release();
    }

    public static void main(String[] args) {
        int maxCustomers = Integer.valueOf(args[0]);

        BarberShop barberShop = new BarberShop(maxCustomers);
        barberShop.openShop();
    }
}
