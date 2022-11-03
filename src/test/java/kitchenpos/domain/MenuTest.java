package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("price가 0미만인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void createProductByPriceNegative(final int price) {
        assertThatThrownBy(() -> new Menu("메뉴", BigDecimal.valueOf(price), 1L,
                List.of(new MenuProduct(1L, 1))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}