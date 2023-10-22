package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuTest {

    @Test
    void throw_when_price_is_under_or_equal_than_zero() {
        // when & then
        assertThatThrownBy(() -> new Menu("chicken", BigDecimal.valueOf(-1L), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price must be greater than zero.");
    }

    @Test
    void throw_when_menu_price_is_greater_than_sum_of_products_price() {
        // given
        final Menu menu = new Menu("chicken", BigDecimal.valueOf(4000L + 1L), null);
        final List<MenuProduct> menuProducts = List.of(
                new MenuProduct(menu, new Product("chicken", Price.from(2000L)).getId(), 1L),
                new MenuProduct(menu, new Product("chicken", Price.from(1000L)).getId(), 2L)
        );

        // when & then
        assertThatThrownBy(() -> menu.applyMenuProducts(menuProducts, Price.from(4000L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Sum of menu products price must be greater than menu price.");
    }
}
