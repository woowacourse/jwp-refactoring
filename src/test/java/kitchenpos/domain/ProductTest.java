package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import kitchenpos.common.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Product 클래스의")
class ProductTest {

    @Nested
    @DisplayName("생성자는")
    class Constructor {

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"-1", "-1000"})
        @DisplayName("상품의 가격이 null이거나 0미만인 경우 예외를 던진다.")
        void price_NullOrLessThanZero_ExceptionThrown(final BigDecimal price) {
            assertThatThrownBy(() -> new Product("두마리치킨", Price.valueOf(price)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
