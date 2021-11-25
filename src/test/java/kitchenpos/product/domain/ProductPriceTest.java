package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.exception.InvalidProductPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductPrice 단위 테스트")
class ProductPriceTest {

    @DisplayName("ProductPrice를 생성할 때")
    @Nested
    class Create {

        @DisplayName("value가 Null일 경우 예외가 발생한다.")
        @Test
        void valueNullException() {
            // when, then
            assertThatThrownBy(() -> new ProductPrice(null))
                .isExactlyInstanceOf(InvalidProductPriceException.class);
        }

        @DisplayName("value가 0보다 작을 경우 예외가 발생한다.")
        @Test
        void valueNegativeException() {
            // when, then
            assertThatThrownBy(() -> new ProductPrice(BigDecimal.valueOf(-1)))
                .isExactlyInstanceOf(InvalidProductPriceException.class);
        }
    }
}
