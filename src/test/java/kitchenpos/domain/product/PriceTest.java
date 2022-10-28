package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Price;

@DisplayName("Price 도메인 테스트")
class PriceTest {

    @DisplayName("상품의 가격은 null 이 아니어야 한다")
    @Test
    void priceIsNull() {
        assertThatThrownBy(() -> new Price(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("상품의 가격은 null 이 아니며 0원 이상이어야 합니다.");
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다")
    @Test
    void priceIsLowerZero() {
        assertThatThrownBy(() -> new Price(new BigDecimal(-1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("상품의 가격은 null 이 아니며 0원 이상이어야 합니다.");
    }
}
