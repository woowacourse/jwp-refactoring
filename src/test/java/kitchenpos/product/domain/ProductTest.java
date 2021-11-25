package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.exception.InvalidProductNameException;
import kitchenpos.product.exception.InvalidProductPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Product 단위 테스트")
class ProductTest {

    @DisplayName("Product를 생성할 때")
    @Nested
    class Create {

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // when, then
            assertThatThrownBy(() -> new Product(null, BigDecimal.valueOf(5_000)))
                .isExactlyInstanceOf(InvalidProductNameException.class);
        }

        @DisplayName("name이 공백뿐인 경우 예외가 발생한다.")
        @Test
        void nameBlankException() {
            // when, then
            assertThatThrownBy(() -> new Product(" ", BigDecimal.valueOf(5_000)))
                .isExactlyInstanceOf(InvalidProductNameException.class);
        }

        @DisplayName("price가 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // when, then
            assertThatThrownBy(() -> new Product("치즈버거", null))
                .isExactlyInstanceOf(InvalidProductPriceException.class);
        }

        @DisplayName("price가 음수인 경우 예외가 발생한다.")
        @Test
        void priceNegativeException() {
            // when, then
            assertThatThrownBy(() -> new Product("치즈버거", BigDecimal.valueOf(-1)))
                .isExactlyInstanceOf(InvalidProductPriceException.class);
        }
    }
}
