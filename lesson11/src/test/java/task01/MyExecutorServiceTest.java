package task01;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
class MyExecutorServiceTest {

    @Test
    public void executeTest() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger(0);

        MyExecutorService es = new MyExecutorService(4);
        es.execute(counter::incrementAndGet);
        es.execute(counter::incrementAndGet);
        es.execute(counter::incrementAndGet);
        es.execute(counter::incrementAndGet);
        es.execute(counter::incrementAndGet);
        es.execute(counter::incrementAndGet);
        es.execute(counter::incrementAndGet);
        es.execute(counter::incrementAndGet);
        es.execute(counter::incrementAndGet);
        es.execute(counter::incrementAndGet);

        Thread.sleep(100);

        assertEquals(10, counter.get());
    }

    @Test
    public void executeTest2() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch cdl = new CountDownLatch(1_000_000);
        MyExecutorService es = new MyExecutorService(1);

        for (int i = 0; i < 1_000_000; i++) {

            if (i == 200000) {
                es.execute(() -> {
                    cdl.countDown();
                    counter.incrementAndGet();
                    throw new IllegalArgumentException("");
                });
            } else {
                es.execute(() -> {
                    counter.incrementAndGet();
                    cdl.countDown();
                });
            }
        }

        cdl.await();
        assertEquals(1000000, counter.get());
    }
}