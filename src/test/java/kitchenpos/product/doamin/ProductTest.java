package kitchenpos.product.doamin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품 가격이 null일 경우 예외 발생")
    void whenProductPriceIsNull() {
        assertThatThrownBy(() -> new Product(1L, "상품", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격이 0원 일때 생성된다.")
    void whenProductPriceIsZero() {
        final String productName = "맥북m1";
        final BigDecimal productPrice = BigDecimal.ZERO;
        final Product product = new Product(1L, productName, productPrice);

        assertAll(
                () -> assertThat(product.getName()).isEqualTo(productName),
                () -> assertThat(product.getPrice()).isEqualByComparingTo(productPrice)
        );
    }

    @Test
    @DisplayName("상품 가격이 0원 미만 일때 예외 발생")
    void whenProductPriceIsUnderZero() {
        assertThatThrownBy(() -> new Product(1L, "상품", BigDecimal.valueOf(-1000L)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
