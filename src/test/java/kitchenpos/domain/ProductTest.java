package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Product 단위 테스트")
class ProductTest {

    @DisplayName("Product를 생성할 때")
    @Nested
    class Construct {

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // given
            String name = null;
            BigDecimal price = BigDecimal.valueOf(5_000);

            // when, then
            assertThatThrownBy(() -> new Product(name, price))
                .isExactlyInstanceOf(InvalidProductException.class);
        }

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // given
            String name = "치즈버거";
            BigDecimal price = null;

            // when, then
            assertThatThrownBy(() -> new Product(name, price))
                .isExactlyInstanceOf(InvalidProductException.class);
        }
    }
}