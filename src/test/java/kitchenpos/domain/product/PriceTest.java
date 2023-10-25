package kitchenpos.domain.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Test
    void 가격은_0이상이어야_한다() {
        // given
        long price = -1L;

        // when & then
        assertThatThrownBy(() -> new Price(price))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0원 이상이어야 한다.");
    }

    @Test
    void 가격_곱셈_테스트() {
        // given
        Price price = new Price(1000L);
        long quantity = 2L;

        // when
        long result = price.multiplyWithQuantity(quantity).longValue();

        // then
        assertThat(result).isEqualTo(2000L);
    }
}
