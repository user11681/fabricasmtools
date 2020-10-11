import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class ATest {
    @Test
    public void test() {
        final long start = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getStackTrace());
        }

        System.out.println((System.nanoTime() - start) / 1000000000D);
    }
}
