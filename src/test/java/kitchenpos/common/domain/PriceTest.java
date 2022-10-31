package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.BadPriceCreatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PriceTest {

    private static final int PRICE_1000 = 1000;
    private static final int INVALID_PRICE = -1;

    @Nested
    @DisplayName("가격 값 객체는")
    class From {

        @Test
        @DisplayName("객체끼리 동등성을 비교할 수 있다.")
        void success() {
            assertThat(Price.from(PRICE_1000)).isEqualTo(Price.from(PRICE_1000));
        }

        @Test
        @DisplayName("생성하려는 가격이 음수일 수 없다.")
        void fail_minimumPrice() {
            assertThatThrownBy(() -> Price.from(INVALID_PRICE))
                .isInstanceOf(BadPriceCreatedException.class);
        }
    }
}
