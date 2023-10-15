package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.support.FixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("새로운 메뉴를 생성한다.")
    @Test
    void create_newMenu() {
        // given
        final MenuGroup menuGroup = FixtureFactory.savedMenuGroup(1L, "메뉴 그룹");
        final Product product = FixtureFactory.savedProduct(1L, "상품1", new BigDecimal(10000));
        final MenuProduct menuProduct = FixtureFactory.savedMenuProduct(1L, 1L, 1L, 2);
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        final Menu newMenu = FixtureFactory.forSaveMenu("새 메뉴", new BigDecimal(20000), menuGroup.getId(), menuProducts);
        final Menu savedMenu = FixtureFactory.savedMenu(1L, "새 메뉴", new BigDecimal(20000), menuGroup.getId(), null);

        given(menuGroupDao.existsById(menuGroup.getId()))
                .willReturn(true);
        given(productDao.findById(any()))
                .willReturn(Optional.of(product));
        given(menuDao.save(newMenu))
                .willReturn(savedMenu);
        // when
        final Menu result = menuService.create(newMenu);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(savedMenu.getId());
            softly.assertThat(result.getName()).isEqualTo(savedMenu.getName());
            softly.assertThat(result.getPrice()).isEqualTo(savedMenu.getPrice());
            softly.assertThat(result.getMenuGroupId()).isEqualTo(savedMenu.getMenuGroupId());
            softly.assertThat(result.getMenuProducts()).isEqualTo(savedMenu.getMenuProducts());
        });
    }

    @DisplayName("메뉴 금액이 음수이면 메뉴를 생성할 수 없습니다.")
    @Test
    void create_fail_menu_price_under_0() {
        // given
        final Menu wrongMenu = FixtureFactory.forSaveMenu("새 메뉴", new BigDecimal(-1000), 1L, null);

        // when
        // then
        assertThatThrownBy(() -> menuService.create(wrongMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 금액이 null이면 메뉴를 생성할 수 없습니다.")
    @Test
    void create_fail_with_menu_price_null() {
        // given
        final Menu wrongMenu = FixtureFactory.forSaveMenu("새 메뉴", null, 1L, null);


        // when
        // then
        assertThatThrownBy(() -> menuService.create(wrongMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹에 포함되어 있지 않은 메뉴이면 메뉴를 생성할 수 없다.")
    @Test
    void create_fail_menu_not_contained_menuGroup() {
        // given
        final Menu wrongMenu = FixtureFactory.forSaveMenu("새 메뉴", new BigDecimal(1000), null, null);

        given(menuGroupDao.existsById(any()))
                .willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> menuService.create(wrongMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품이 존재하지 않는 상품이면 메뉴를 생성할 수 없다.")
    @Test
    void create_fail_menu_contain_notExistProduct() {
        // given
        final MenuGroup menuGroup = FixtureFactory.savedMenuGroup(1L, "메뉴 그룹");

        final MenuProduct menuProduct = FixtureFactory.savedMenuProduct(1L, null, null, 2);
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        final Menu wrongMenu = FixtureFactory.forSaveMenu("새 메뉴", new BigDecimal(20000), menuGroup.getId(), menuProducts);

        given(menuGroupDao.existsById(menuGroup.getId()))
                .willReturn(true);
        given(productDao.findById(menuProduct.getProductId()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> menuService.create(wrongMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품들의 가격 총합보다 메뉴 가격이 크면 메뉴를 만들 수 없다.")
    @Test
    void create_fail_menuPrice_expensive_than_all_product_price() {
        // given
        final MenuGroup menuGroup = FixtureFactory.savedMenuGroup(1L, "메뉴 그룹");
        final Product product = FixtureFactory.savedProduct(1L, "상품1", new BigDecimal(10000));

        final MenuProduct menuProduct = FixtureFactory.savedMenuProduct(null, null, product.getId(), 2);
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        final Menu wrongMenu = FixtureFactory.forSaveMenu("새 메뉴", new BigDecimal(50000), menuGroup.getId(), menuProducts);

        given(menuGroupDao.existsById(menuGroup.getId()))
                .willReturn(true);
        given(productDao.findById(menuProduct.getProductId()))
                .willReturn(Optional.of(product));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(wrongMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴를 조회할 수 있다.")
    @Test
    void find_all_menus() {
        // given
        final MenuProduct menuProduct1 = FixtureFactory.savedMenuProduct(1L, 1L, 1L, 2);
        final MenuProduct menuProduct2 = FixtureFactory.savedMenuProduct(2L, 2L, 1L, 2);
        final Menu menu1 = FixtureFactory.savedMenu(1L, "메뉴 1", new BigDecimal(10000), 1L, List.of());
        final Menu menu2 = FixtureFactory.savedMenu(2L, "메뉴 2", new BigDecimal(20000), 1L, List.of());

        final List<Menu> menus = List.of(menu1, menu2);

        given(menuDao.findAll())
                .willReturn(menus);

        given(menuProductDao.findAllByMenuId(any()))
                .willReturn(List.of(menuProduct1))
                .willReturn(List.of(menuProduct2));

        // when
        final List<Menu> result = menuService.list();

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(menus);
    }
}
