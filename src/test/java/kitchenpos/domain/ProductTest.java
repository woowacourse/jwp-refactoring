package kitchenpos.domain;

import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    @Nested
    @DisplayName("Product를 생성할 때 ")
    class CreateTest {

        @ParameterizedTest
        @ValueSource(longs = {-1, -100})
        @DisplayName("가격이 올바르지 않을 경우 예외가 발생한다.")
        void createPriceFailed(final Long price) {
            assertThatThrownBy(() -> new Product(PRODUCT1_NAME, BigDecimal.valueOf(price)))
                    .isInstanceOf(InvalidProductException.class);
        }

        @Test
        @DisplayName("예외가 없을 경우 정상적으로 생성된다.")
        void create() {
            assertDoesNotThrow(() -> new Product(1L, PRODUCT1_NAME, PRODUCT2_PRICE));
        }
    }
}
