package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void fail_noPrice() {
        Assertions.assertThatThrownBy(() -> new Menu(1L, "menuA", null, 1L,
                        Arrays.asList(new MenuProduct(1L, null, 1L, 1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void fail_negativePrice() {
        Assertions.assertThatThrownBy(() -> new Menu(1L, "menuA", BigDecimal.valueOf(-1), 1L,
                        Arrays.asList(new MenuProduct(1L, null, 1L, 1))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
