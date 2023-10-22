package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.createMenu;
import static kitchenpos.application.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.application.fixture.MenuProductFixture.createMenuProduct;
import static kitchenpos.application.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 생성, 저장한다.")
    @Test
    void create_success() {
        // given
        final MenuGroup menuGroup = createMenuGroup(1L, "menuGroup");
        final Product product = createProduct(1L, "product", 1000L);
        final Menu menu = createMenu(1L, "menu", 500L, menuGroup.getId());
        final MenuProduct menuProduct = createMenuProduct(product.getId(), 2, menu.getId());
        menu.addMenuProducts(List.of(menuProduct));

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(true);
        given(productDao.findById(anyLong()))
            .willReturn(Optional.of(product));
        given(menuDao.save(any(Menu.class)))
            .willReturn(menu);
        given(menuProductDao.save(any(MenuProduct.class)))
            .willReturn(menuProduct);

        // when
        final Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu).isEqualTo(menu);
    }

    @DisplayName("메뉴를 저장 시, 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void create_price_fail() {
        // given
        final MenuGroup menuGroup = createMenuGroup(1L, "menuGroup");
        final Menu menu = createMenu(1L, "menu", -1L, menuGroup.getId());

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴 그룹이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_menuGroup_fail() {
        // given
        final Menu menu = createMenu(1L, "menu", 1000L, 0L);

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴의 상품이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_menuProduct_fail() {
        // given
        final MenuGroup menuGroup = createMenuGroup(1L, "menuGroup");
        final Menu menu = createMenu(1L, "menu", 1000L, menuGroup.getId());
        final MenuProduct menuProduct = createMenuProduct(0L, 2, menu.getId());
        menu.addMenuProducts(List.of(menuProduct));

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(true);
        given(productDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴의 총 가격이 상품 총 가격보다 크다면 예외가 발생한다.")
    @Test
    void create_menuTotalPrice_fail() {
        // given
        final Product product = createProduct(1L, "product", 1000L);
        final MenuGroup menuGroup = createMenuGroup(1L, "menuGroup");
        final Menu menu = createMenu(1L, "menu", 3000L, menuGroup.getId());
        final MenuProduct menuProduct = createMenuProduct(product.getId(), 2, menu.getId());
        menu.addMenuProducts(List.of(menuProduct));

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(true);
        given(productDao.findById(anyLong()))
            .willReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list() {
        // given
        final MenuGroup menuGroup = createMenuGroup(1L, "menuGroup");
        final Product product = createProduct(1L, "product", 1000L);
        final Menu menu1 = createMenu(1L, "menu", 500L, menuGroup.getId());
        final MenuProduct menuProduct = createMenuProduct(product.getId(), 2, menu1.getId());
        menu1.addMenuProducts(List.of(menuProduct));

        final Menu menu2 = createMenu(2L, "menu2", 1000L, menuGroup.getId());
        final MenuProduct menuProduct2 = createMenuProduct(product.getId(), 3, menu2.getId());
        menu2.addMenuProducts(List.of(menuProduct2));

        given(menuDao.findAll())
            .willReturn(List.of(menu1, menu2));

        // when
        final List<Menu> foundMenus = menuService.list();

        // then
        assertThat(foundMenus).containsExactly(menu1, menu2);
    }
}
