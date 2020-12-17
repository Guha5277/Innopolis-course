package task01;

import org.junit.jupiter.api.Test;

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
}