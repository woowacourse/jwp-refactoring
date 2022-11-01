package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Menu의")
class MenuTest {

    @Nested
    @DisplayName("생성자는")
    class Constructor {
        @Test
        @DisplayName("가격이 null이면, 예외를 던진다.")
        void fail_noPrice() {
            Assertions.assertThatThrownBy(() -> new Menu(1L, "menuA", null, 1L,
                            Arrays.asList(new MenuProduct(1L, null, 1L, 1))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수면, 예외를 던진다.")
        void fail_negativePrice() {
            Assertions.assertThatThrownBy(() -> new Menu(1L, "menuA", BigDecimal.valueOf(-1), 1L,
                            Arrays.asList(new MenuProduct(1L, null, 1L, 1))))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
