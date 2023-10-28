package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 가격에_수량을_곱한_값을_구한다() {
        // given
        final Product product = new Product("상품", BigDecimal.valueOf(10000));
        final long quantity = 2;

        // when
        final BigDecimal result = product.multiplyByQuantity(quantity);

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(20000));
    }
}
