package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.IllegalPriceException;
import kitchenpos.fixtures.ProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("수량에 따른 전체 가격을 구한다")
    void calculateTotalPrice() {
        final Product product = ProductFixtures.CHICKEN.createWithPrice(new BigDecimal(10_000));

        final BigDecimal totalPrice = product.calculateTotalPrice(2);

        assertThat(totalPrice).isEqualByComparingTo(new BigDecimal(20_000));
    }

    @Test
    @DisplayName("상품의 가격은 비어 있을 수 없다")
    void validateNullPrice() {
        final Product product = ProductFixtures.CHICKEN.createWithPrice(null);

        assertThatThrownBy(product::validatePrice)
                .isExactlyInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("상품의 가격은 0원보다 작을 수 없다")
    void validateNegativePrice() {
        final Product product = ProductFixtures.CHICKEN.createWithPrice(new BigDecimal(-1));

        assertThatThrownBy(product::validatePrice)
                .isExactlyInstanceOf(IllegalPriceException.class);
    }
}
