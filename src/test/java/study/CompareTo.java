package study;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class CompareTo {

    @Test
    void test() {
        BigDecimal bigDecimal1 = new BigDecimal(3);
        BigDecimal bigDecimal2 = new BigDecimal(5);

        assertThat(bigDecimal1.compareTo(bigDecimal2)).isLessThan(0);
    }
}
