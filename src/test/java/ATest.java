import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import user11681.huntinghamhills.Mapper;

@Testable
public class ATest {
    @Test
    public void mapper() {
        final Mapper mapper = new Mapper();
    }

    @Test
    public void stackTrace() {
        final long start = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getStackTrace());
        }

        System.out.println((System.nanoTime() - start) / 1000000000D);
    }
}
