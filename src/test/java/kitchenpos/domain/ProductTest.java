package kitchenpos.domain;

import static kitchenpos.support.ProductFixture.PRODUCT_PRICE_10000;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 제품_가격에_특정_수를_곱한_가격을_반환한다() {
        // given
        final Product product = PRODUCT_PRICE_10000.생성();

        // when
        final Price multipliedPrice = product.multiplyPriceWith(2);

        // then
        assertThat(multipliedPrice).isEqualTo(new Price(new BigDecimal(20_000)));
    }
}
