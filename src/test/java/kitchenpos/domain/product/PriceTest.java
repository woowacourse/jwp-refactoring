package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Price;

@DisplayName("Price 도메인 테스트")
class PriceTest {

    @DisplayName("가격은 null 이 아니어야 한다")
    @Test
    void priceIsNull() {
        assertThatThrownBy(() -> new Price(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("상품의 가격은 null 이 아니며 0원 이상이어야 합니다.");
    }

    @DisplayName("가격은 0원 이상이어야 한다")
    @Test
    void priceIsLowerZero() {
        assertThatThrownBy(() -> new Price(new BigDecimal(-1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("상품의 가격은 null 이 아니며 0원 이상이어야 합니다.");
    }

    @DisplayName("가격의 곱을 구한다")
    @Test
    void multiply() {
        final Price price = new Price(new BigDecimal(15_000));

        assertThat(price.multiply(2)).isEqualTo(new BigDecimal(30_000));
    }

    @DisplayName("입력받은 가격보다 큰지 확인한다")
    @Test
    void isHigherThan() {
        final Price price = new Price(new BigDecimal(15_000));

        assertThat(price.isHigherThan(new BigDecimal(30_000))).isFalse();
    }
}
