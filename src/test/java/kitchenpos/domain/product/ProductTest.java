package kitchenpos.domain.product;

import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1_NAME;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("수량을 받아서 총 가격을 반환한다.")
    void calculateTotalPrice() {
        // given
        final BigDecimal price = PRODUCT1_PRICE;
        final long quantity = 2L;
        final Product product = new Product(PRODUCT1_NAME, price);
        final BigDecimal expected = price.multiply(BigDecimal.valueOf(quantity));

        // when
        BigDecimal actual = product.calculateTotalPrice(quantity);

        // then
        assertThat(actual).isEqualTo(expected);
    }

}
