package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    @Test
    void 상품의_가격은_0원_이상이어야_한다() {
        final Product product = new Product();

        assertThatThrownBy(() -> product.setPrice(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
