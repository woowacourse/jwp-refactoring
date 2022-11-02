package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.MenuProductFixtures;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void constructMenu() {
        // given
        BigDecimal price = BigDecimal.valueOf(2500);
        String name = "메뉴";
        long menuGroupId = 1L;
        List<MenuProduct> menuProducts = List.of(
                MenuProductFixtures.createMenuProduct(1L, 1000L, 2),
                MenuProductFixtures.createMenuProduct(1L, 1000L, 2)
        );

        // when
        Menu menu = new Menu(
                name,
                price,
                menuGroupId,
                menuProducts
        );

        // then
        assertThat(menu).isNotNull();
    }

    @Test
    void constructMenuExpensiveThanSumOfPrices() {
        // given
        BigDecimal price = BigDecimal.valueOf(5000);

        // when & then
        assertThatThrownBy(() -> new Menu(
                        "메뉴",
                        price,
                        1L,
                        List.of(
                                MenuProductFixtures.createMenuProduct(1L, 1000L, 1),
                                MenuProductFixtures.createMenuProduct(1L, 1000L, 2)
                        )
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
