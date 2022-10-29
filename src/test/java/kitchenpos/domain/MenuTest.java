package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.LinkedList;
import kitchenpos.domain.menu.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @ParameterizedTest(name = "가격이 음수일 경우 메뉴를 생성하면 예외가 발생한다.")
    @ValueSource(ints = {-1000, -1})
    void createWithNegativePriceMenu(int price) {
        assertThatThrownBy(() -> Menu.of(1L, "추천메뉴", BigDecimal.valueOf(price), 1L, new LinkedList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴안의 상품들의 가격의 합을 넘을 경우 메뉴를 생성하면 예외가 발생한다.")
    void createWithOverSumProductsPrice() {
        final LinkedList<MenuProduct> menuProducts = new LinkedList<>();
        menuProducts.add(new MenuProduct(1L, 1L, 2, BigDecimal.valueOf(10000)));
        assertThatThrownBy(() -> Menu.of(1L, "추천메뉴", BigDecimal.valueOf(21000), 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 메뉴 상품의 가격의 합을 넘을 수 없습니다.");
    }
}