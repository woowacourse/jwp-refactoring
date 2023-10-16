package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.Fixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuServiceTest extends ServiceBaseTest {

    @Autowired
    protected MenuService menuService;

    private MenuGroup menuGroup;
    private Product product;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupDao.save(Fixture.menuGroup("메뉴 그룹"));
        product = productDao.save(Fixture.product("상품", 1000));
        menuProduct = Fixture.menuProduct(null, product.getId(), 3L);
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void create() {
        //given
        final Menu menu = Fixture.menu("메뉴1", 1000, menuGroup.getId(), List.of(menuProduct));

        //when
        final Menu createdMenu = menuService.create(menu);

        //then
        assertAll(
                () -> assertThat(createdMenu.getId()).isNotNull(),
                () -> assertThat(createdMenu.getName()).isEqualTo(menu.getName())
        );
    }

    @Test
    @DisplayName("메뉴 가격은 0원 이상이다.")
    void menuPriceOverZero() {
        //given
        final Menu menu = Fixture.menu("메뉴1", -1, menuGroup.getId(), List.of(menuProduct));

        //when&then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴는 존재하는 메뉴 그룹에 속해있어야 한다.")
    void menuBelongToValidMenuGroup() {
        //given
        final Menu menu = Fixture.menu("메뉴1", 0, 999L, List.of(menuProduct));

        //when&then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴는 존재하는 상품만 가져야 한다.")
    void menuHasValidMenu() {
        //given
        final MenuProduct menuProduct1 = Fixture.menuProduct(null, 999L, 999L);
        final Menu menu = Fixture.menu("메뉴1", 0, menuGroup.getId(), List.of(menuProduct1));

        //when&then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격은 상품의 합계 이하이다.")
    void menuPriceSameOrOverThanProductsPrices() {
        //given
        final Menu menu = Fixture.menu("메뉴1", 5000, menuGroup.getId(), List.of(menuProduct));

        //when&then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void list() {
        //given
        final Menu menu = Fixture.menu("메뉴1", 1000, menuGroup.getId(), List.of(menuProduct));
        final Menu savedMenu = menuService.create(menu);

        //when
        final List<Menu> menus = menuService.list();

        //then
        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menus.get(0).getName()).isEqualTo(savedMenu.getName())
        );
    }
}
