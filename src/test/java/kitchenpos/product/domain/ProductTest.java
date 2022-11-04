package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품 생성자")
    @Nested
    class Constructor {

        @DisplayName("가격이 0보다 작다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceIsLessThan0() {
            // given & when & then
            assertThatThrownBy(() -> new Product("상품", BigDecimal.valueOf(-1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
