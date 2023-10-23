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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.menu.MenuRequest;
import kitchenpos.application.dto.menu.MenuResponse;
import kitchenpos.application.dto.menu.ProductQuantityDto;
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
        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(500L), menuGroup.getId(), List.of(
            new ProductQuantityDto(product.getId(), 2)
        ));
        final Menu menu = createMenu(1L, "menu", 500L, menuGroup.getId());
        final MenuProduct menuProduct = createMenuProduct(product.getId(), 2, menu.getId());

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(true);
        given(productDao.findById(anyLong()))
            .willReturn(Optional.of(product));
        given(menuDao.save(any(Menu.class)))
            .willReturn(menu);
        given(menuProductDao.save(any(MenuProduct.class)))
            .willReturn(menuProduct);

        // when
        final MenuResponse savedMenu = menuService.create(menuRequest);

        // then
        assertThat(savedMenu).isNotNull();
    }

    @DisplayName("메뉴를 저장 시, 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void create_price_fail() {
        // given
        final MenuGroup menuGroup = createMenuGroup(1L, "menuGroup");
        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(-1L), menuGroup.getId(), Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴 그룹이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_menuGroup_fail() {
        // given
        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(500L), 1L, Collections.emptyList());

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴의 상품이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_menuProduct_fail() {
        // given
        final MenuGroup menuGroup = createMenuGroup(1L, "menuGroup");
        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(500L), menuGroup.getId(), List.of(
            new ProductQuantityDto(0L, 2)
        ));

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(true);
        given(productDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴의 총 가격이 상품 총 가격보다 크다면 예외가 발생한다.")
    @Test
    void create_menuTotalPrice_fail() {
        // given
        final Product product = createProduct(1L, "product", 1000L);
        final MenuGroup menuGroup = createMenuGroup(1L, "menuGroup");
        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(2001L), menuGroup.getId(), List.of(
            new ProductQuantityDto(product.getId(), 2)
        ));

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(true);
        given(productDao.findById(anyLong()))
            .willReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
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
        final List<MenuResponse> foundMenus = menuService.list();

        // then
        assertThat(foundMenus).usingRecursiveComparison()
            .isEqualTo(List.of(MenuResponse.from(menu1), MenuResponse.from(menu2)));
    }
}
