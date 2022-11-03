package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴를 생성할 때 가격이 null이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_null() {
        List<MenuProduct> menuProducts = Collections.singletonList(
                new MenuProduct(1L, 2));

        assertThatThrownBy(() -> Menu.builder()
                .name("후라이드+후라이드")
                .price(null)
                .menuGroupId(1L)
                .menuProducts(new MenuProducts(menuProducts))
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 때 가격이 음수이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_negative() {
        List<MenuProduct> menuProducts = Collections.singletonList(
                new MenuProduct(1L, 2));

        assertThatThrownBy(() -> Menu.builder()
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(-1L))
                .menuGroupId(1L)
                .menuProducts(new MenuProducts(menuProducts))
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
