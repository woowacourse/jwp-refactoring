package kitchenpos.domain;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Product의")
class ProductTest {

    @Test
    @DisplayName("가격은 존재해야 한다.")
    void fail_existPrice() {
        Assertions.assertThatThrownBy(() -> new Product(1L, "productA", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격은 0보다 크거나 같아야 한다.")
    void fail_negativePrice() {
        Assertions.assertThatThrownBy(() -> new Product(1L, "productA", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
