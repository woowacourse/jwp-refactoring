package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ProductFixtures;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void constructMenu() {
        // given
        BigDecimal price = BigDecimal.valueOf(2500);
        String name = "메뉴";
        long menuGroupId = 1L;
        List<MenuProduct> menuProducts = List.of(
                new MenuProduct(ProductFixtures.createProduct(), 1L),
                new MenuProduct(ProductFixtures.createProduct(), 2L)
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
                                new MenuProduct(ProductFixtures.createProduct(), 1L),
                                new MenuProduct(ProductFixtures.createProduct(), 2L)
                        )
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
