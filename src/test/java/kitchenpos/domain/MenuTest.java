package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴가격이_상품의_총_가격보다_크면_예외를_발생한다() {
        // given
        final MenuProduct menuProduct = new MenuProduct(null, new Product("야끼만두", new Price(new BigDecimal(1_000))), 2);

        // when, then
        assertThatThrownBy(() -> new Menu("모둠만두", new Price(new BigDecimal(2_100)), 1L, List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
