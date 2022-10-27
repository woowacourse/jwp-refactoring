package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {
    @Test
    @DisplayName("메뉴를 생성한다")
    void create() {
        saveAndGetProduct(1L);
        final Menu menu = saveAndGetMenu(1L);
        menu.addMenuProduct(saveAndGetMenuProduct(1L, 1L, 1L));

        final Menu actual =
                menuService.create(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menu.getMenuProducts());

        assertThat(actual.getName()).isEqualTo("피자세트메뉴");
    }

    @Test
    @DisplayName("메뉴 가격이 0 미만이면 예외를 반환한다")
    void create_priceException() {
        saveAndGetProduct(1L);
        final Menu menu = saveAndGetMenu(1L);
        menu.addMenuProduct(saveAndGetMenuProduct(1L, 1L, 1L));

        assertThatThrownBy(() -> menuService.create(
                menu.getName(), BigDecimal.valueOf(-1L), menu.getMenuGroupId(), menu.getMenuProducts())
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 상품 가격의 합보다 크면 예외를 반환한다")
    void create_priceExpensiveException() {
        final Menu actual = createMenuRequest(BigDecimal.valueOf(999999999L), 1L);

        assertThatThrownBy(() -> menuService.create(
                actual.getName(), actual.getPrice(), actual.getMenuGroupId(), actual.getMenuProducts())
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 포함된 상품이 존재하지 않는 상품이면 예외를 반환한다")
    void create_notExistProductException() {
        final Menu actual = createMenuRequestNotExistProduct(BigDecimal.valueOf(15_000L), 1L);

        assertThatThrownBy(() -> menuService.create(
                actual.getName(), actual.getPrice(), actual.getMenuGroupId(), actual.getMenuProducts())
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 전체를 조회한다")
    void list() {
        saveAndGetMenu(1L);

        final List<Menu> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual)
                        .extracting("id")
                        .containsExactly(1L)
        );
    }

    private Menu createMenuRequest(final BigDecimal price, final Long menuId) {
        final MenuGroup menuGroup = saveAndGetMenuGroup(1L);
        final Menu menu = new Menu(menuId, "치킨메뉴", price, menuGroup.getId(), new ArrayList<>());

        final Product product = saveAndGetProduct(1L);
        final MenuProduct menuProduct = saveAndGetMenuProduct(1L, menu.getId(), product.getId());
        menu.addMenuProduct(menuProduct);

        return menuDao.save(menu);
    }

    private Menu createMenuRequestNotExistProduct(final BigDecimal price, final Long menuId) {
        final MenuGroup menuGroup = saveAndGetMenuGroup(1L);
        final Menu menu = new Menu(menuId, "치킨메뉴", price, menuGroup.getId(), new ArrayList<>());

        final MenuProduct menuProduct = saveAndGetMenuProduct(1L, menu.getId(), 999999999L);
        menu.addMenuProduct(menuProduct);

        return menuDao.save(menu);
    }
}
