package kitchenpos.menu.domain;

import static org.assertj.core.api.
Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @ParameterizedTest
    @ValueSource(longs = {2L, 1L})
    void validatePrice(final long price) {
        // given
        final Product productPriceOne = new Product("product1", BigDecimal.ONE);
        final MenuProduct menuProduct = new MenuProduct(productPriceOne, 2L);
        final MenuProducts menuProducts = new MenuProducts(List.of(menuProduct));
        final MenuGroup menuGroup = new MenuGroup("group");

        // when
        final Menu menu = new Menu("name", BigDecimal.valueOf(price), menuGroup, menuProducts);

        // then
        assertThat(menu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(price));
    }

    @ParameterizedTest
    @ValueSource(longs = {3L, -1L})
    void validatePrice_invalidException(final long price) {
        // given
        final Product productPriceOne = new Product("product1", BigDecimal.ONE);
        final MenuProduct menuProduct = new MenuProduct(productPriceOne, 2L);
        final MenuProducts menuProducts = new MenuProducts(List.of(menuProduct));
        final MenuGroup menuGroup = new MenuGroup("group");

        // when & then
        assertThatThrownBy(() -> new Menu("name", BigDecimal.valueOf(price), menuGroup, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
