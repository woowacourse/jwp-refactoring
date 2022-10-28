package kitchenpos.application;

import static kitchenpos.application.DomainFixture.getMenu;
import static kitchenpos.application.DomainFixture.getMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {


    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        final List<MenuProduct> menuProducts = createMenuProducts();
        final Menu menu = getMenu(menuGroup.getId(), menuProducts);

        final Menu savedMenu = 메뉴_등록(menu);

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualByComparingTo(menu.getPrice()),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(savedMenu.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴를 등록한다. - 메뉴 그룹이 존재하지 않으면 예외를 반환한다.")
    @Test
    void create_exception_noSuchMenuGroup() {
        final List<MenuProduct> menuProducts = createMenuProducts();
        final Menu menu = getMenu(null, menuProducts);

        assertThatThrownBy(() -> 메뉴_등록(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록한다. - 존재하지 않는 상품이 포함되어 있으면 예외를 반환한다.")
    @Test
    void create_exception_noSuchProduct() {
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(null, 1, BigDecimal.valueOf(800)));
        final Menu menu = getMenu(menuGroup.getId(), menuProducts);

        assertThatThrownBy(() -> 메뉴_등록(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        final Menu menu = getMenu(menuGroup.getId(), createMenuProducts());
        메뉴_등록(menu);

        final List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(1);
    }
}
