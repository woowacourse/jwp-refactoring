package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 상품의_가격에_상품수량을_곱한다() {
        final Product product = new Product("반반치킨", new ProductPrice(BigDecimal.valueOf(16000)));

        assertThat(product.multiplyQuantity(3L))
                .isEqualByComparingTo(BigDecimal.valueOf((48000)));
    }
}
