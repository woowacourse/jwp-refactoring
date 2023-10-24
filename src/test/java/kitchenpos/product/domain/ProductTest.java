package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("가격을 곱한다.")
    void multiplyPrice() {
        // given
        final Product product = new Product("name", BigDecimal.TEN);

        // when
        final BigDecimal price = product.multiplyPrice(BigDecimal.valueOf(4));

        // then
        assertThat(price).isEqualTo(BigDecimal.valueOf(40));
    }
}
