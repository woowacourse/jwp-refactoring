package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("가격은 비어있을 수 없다")
    void nullPrice() {
        // given

        // when, then
        assertThatThrownBy(() -> new Product("test", null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격은 음수일 수 없다")
    void minusPrice() {
        // given

        // when, then
        assertThatThrownBy(() -> new Product("test", BigDecimal.valueOf(-100)))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
