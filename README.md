# FIFO Barbershop Problem
### Two different solutions to solve the FIFO Barbershop problem from The Little Book of Semaphores

Both solutions are implemented in Java, each one using different components.

+ **barbershopConc** uses a *LinkedBlockingQueue* to guarantee the Customers access and a *SynchronousQueue* to control the Barber work

+ **barbershopMutex** uses some *Semaphores* to control the Customers access to a *Queue* and to the Barber, who "processes" the queue.
