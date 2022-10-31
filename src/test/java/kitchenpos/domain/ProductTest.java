package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void construct() {
        // given
        String name = "치킨";
        BigDecimal price = BigDecimal.valueOf(1000);
        // when
        Product product = new Product(name, price);
        // then
        assertThat(product).isNotNull();
    }

    @Test
    void constructWithNegativePrice() {
        // given
        String name = "치킨";
        BigDecimal price = BigDecimal.valueOf(-1);
        // when & then
        assertThatThrownBy(() -> new Product(name, price))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
