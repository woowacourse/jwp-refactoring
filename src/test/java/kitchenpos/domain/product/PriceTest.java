package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import kitchenpos.common.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Price 는 ")
class PriceTest {

    @DisplayName("생성 시")
    @Nested
    class PriceCreationTest {

        @DisplayName("생성 가능한 가격이면 에러를 던지지 않는다.")
        @Test
        void createPriceSuccess() {
            assertDoesNotThrow(() -> new Price(BigDecimal.valueOf(1000L)));
        }

        @DisplayName("생성 불가능한 가격이면 에러를 던진다.")
        @Test
        void createPriceFail() {
            assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1L)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
