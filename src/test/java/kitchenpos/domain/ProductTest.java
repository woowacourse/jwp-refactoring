package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("상품 도메인의")
class ProductTest {

    @Nested
    @DisplayName("정적 팩토리 메서드 of는")
    class Of {

        @Test
        @DisplayName("가격이 null일 수 없다.")
        void of_priceIsNull_exception() {
            // when & then
            assertThatThrownBy(() -> Product.of("치킨", null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수일 수 없다.")
        void of_priceIsNegative_exception() {
            // when & then
            assertThatThrownBy(() -> Product.of("치킨", BigDecimal.valueOf(-1L)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
