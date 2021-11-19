package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.Fixtures;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class MenuServiceTest extends IntegrationTest {

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        // given
        Menu menu = Fixtures.menu(1L, "메뉴", 5000L, 1L, menuProducts());

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getPrice().longValue()).isEqualTo(menu.getPrice().longValue());
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        List<MenuProduct> menuProducts = savedMenu.getMenuProducts();
        assertThat(menuProducts).hasSize(1);
    }

    @DisplayName("메뉴의 가격은 null일 수 없다")
    @Test
    void create_fail_menuPriceCannotBeNull() {
        // given
        Menu menu = Fixtures.menu(1L, "메뉴", null, 1L, menuProducts());

        // when, then
        assertThatCode(() -> menuService.create(menu))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴의 가격은 음수일 수 없다")
    @Test
    void create_fail_menuPriceCannotBeNegative() {
        // given
        Menu menu = Fixtures.menu(1L, "메뉴", -1L, 1L, menuProducts());

        // when, then
        assertThatCode(() -> menuService.create(menu))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴의 메뉴그룹id에 해당하는 메뉴그룹이 존재해야 한다")
    @Test
    void create_fail_menuGroupShouldExistsWhereInMenusMenuGroupId() {
        // given
        Menu menu = Fixtures.menu(1L, "메뉴", 1000L, -1L, menuProducts());

        // when, then
        assertThatCode(() -> menuService.create(menu))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴의 메뉴프로덕츠는 null일 수 없다")
    @Test
    void create_fail_menuMenuProductsCannotBeNull() {
        // given
        Menu menu = Fixtures.menu(1L, "메뉴", 1000L, 1L, null);

        // when, then
        assertThatCode(() -> menuService.create(menu))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴의 가격이 상품들의 가격의 총합보다 같거나 작아야 한다")
    @Test
    void create_fail_menuPriceShouldLowerThanTotalProductsPrice() {
        // given
        Product savedProduct = productService.create(ProductFixture.productForCreate("product", 1000L));
        MenuProduct menuProduct = Fixtures.menuProduct(1, 1, savedProduct.getId(), 1);
        Menu menu = Fixtures.menu(1L, "메뉴", 1001L, 1L, Arrays.asList(menuProduct));

        // when, then
        assertThatCode(() -> menuService.create(menu))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴의 가격이 상품들의 가격의 총합보다 같거나 작아야 한다")
    @ParameterizedTest
    @ValueSource(longs = {1000L, 999L})
    void create_success_menuPriceIsLowerThanTotalProductsPrice(Long menuPrice) {
        // given
        Product savedProduct = productService.create(ProductFixture.productForCreate("product", 1000L));
        MenuProduct menuProduct = Fixtures.menuProduct(1, 1, savedProduct.getId(), 1);
        Menu menu = Fixtures.menu(1L, "메뉴", menuPrice, 1L, Arrays.asList(menuProduct));

        // when, then
        assertThatCode(() -> menuService.create(menu))
                .doesNotThrowAnyException();
    }

    @DisplayName("메뉴 목록을 조회 한다")
    @Test
    void list() {
        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(6);
    }

    private List<MenuProduct> menuProducts() {
        MenuProduct menuProduct = Fixtures.menuProduct(1, 1, 1, 1);
        return Collections.singletonList(menuProduct);
    }
}