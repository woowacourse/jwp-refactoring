package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @DisplayName("메뉴 가격이 상품 총 가격보다 크다면 메뉴에 상품을 등록할 수 없다.")
    @Test
    void addMenuProducts_fail() {
        // given
        final MenuGroup menuGroup = new MenuGroup("menuGroup");
        final Menu menu = new Menu("menu", Price.from(BigDecimal.valueOf(2001L)), menuGroup);
        final Product product1 = new Product("product1", Price.from(BigDecimal.valueOf(1000L)));
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(menu, product1, 2));

        // when, then
        assertThatThrownBy(() -> menu.addMenuProducts(menuProducts))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴 가격은 메뉴 상품 가격 총합보다 클 수 없습니다.");
    }

    @DisplayName("메뉴 가격이 상품 총 가격보다 작거나 같다면 메뉴에 상품이 등록된다.")
    @ParameterizedTest
    @ValueSource(longs = {2000, 1999})
    void addMenuProducts_success(final long menuPrice) {
        // given
        final MenuGroup menuGroup = new MenuGroup("menuGroup");
        final Menu menu = new Menu("menu", Price.from(BigDecimal.valueOf(menuPrice)), menuGroup);
        final Product product1 = new Product("product1", Price.from(BigDecimal.valueOf(1000L)));
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(menu, product1, 2));

        // when
        menu.addMenuProducts(menuProducts);

        // then
        assertThat(menu.getMenuProducts()).hasSize(1);
    }
}
