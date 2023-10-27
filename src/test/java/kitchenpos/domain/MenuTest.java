package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @DisplayName("메뉴를 만든다.")
    @ParameterizedTest
    @ValueSource(longs = {2000, 1999})
    void addMenuProducts_success(final long menuPrice) {
        // given
        final MenuGroup menuGroup = new MenuGroup("menuGroup");
        final Product product1 = new Product("product1", Price.from(BigDecimal.valueOf(1000L)));
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(product1.getId(), 2));

        // when
        final Menu menu = Menu.of("menu", Price.from(BigDecimal.valueOf(menuPrice)), menuGroup.getId(), menuProducts);

        // then
        assertThat(menu.getMenuProducts()).hasSize(1);
    }

    @DisplayName("메뉴의 가격이 주어진 가격보다 크다면 true, 아니면 false가 반환된다.")
    @ParameterizedTest
    @CsvSource(value = {"2001:true", "2000:false", "1999:false"}, delimiter = ':')
    void hasGreaterPriceThan(final long menuPrice, final boolean expected) {
        // given
        final Product product1 = new Product("product1", Price.from(BigDecimal.valueOf(1000L)));
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(product1.getId(), 2));
        final Menu menu = Menu.of("menu", Price.from(BigDecimal.valueOf(menuPrice)), 1L, menuProducts);
        final Price totalMenuProductsPrice = Price.from(BigDecimal.valueOf(2000L));

        // when
        final boolean result = menu.hasGreaterPriceThan(totalMenuProductsPrice);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
