package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 항목의 가격 수량을 구할 수 있다.")
    void calculateAmount() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 2L, 1L, new Price(1000L));

        // when
        final Price actual = menuProduct.calculateAmount();

        // then
        assertThat(actual).isEqualTo(new Price(2000L));
    }
}
