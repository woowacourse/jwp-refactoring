package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.InvalidMenuPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴 항목의 가격 수량의 합보다 메뉴 가격이 비싸면 예외를 반환한다.")
    void construct_menuPriceIsExpansiveThanMenuProductAmountSum() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(1L, 1L, null, new Price(1000L));
        final MenuProduct menuProduct2 = new MenuProduct(2L, 2L, null, new Price(1000L));
        final List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        // when, then
        assertThatThrownBy(() -> new Menu("name", new Price(3001L), 1L, menuProducts))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
    }
}
