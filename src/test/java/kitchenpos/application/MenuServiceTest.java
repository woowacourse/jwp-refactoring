package kitchenpos.application;

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
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("메뉴 그룹");

        final Product product = new Product();
        product.setId(1L);
        product.setName("상품1");
        product.setPrice(new BigDecimal(10000));

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menuProduct.setSeq(1L);
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        final Menu newMenu = new Menu();
        newMenu.setId(1L);
        newMenu.setName("새 메뉴");
        newMenu.setPrice(new BigDecimal(20000));
        newMenu.setMenuGroupId(menuGroup.getId());
        newMenu.setMenuProducts(menuProducts);

        given(menuGroupDao.existsById(menuGroup.getId()))
                .willReturn(true);
        given(productDao.findById(any()))
                .willReturn(Optional.of(product));
        given(menuDao.save(newMenu))
                .willReturn(newMenu);
        // when
        final Menu result = menuService.create(newMenu);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(newMenu.getId());
            softly.assertThat(result.getName()).isEqualTo(newMenu.getName());
            softly.assertThat(result.getPrice()).isEqualTo(newMenu.getPrice());
            softly.assertThat(result.getMenuGroupId()).isEqualTo(newMenu.getMenuGroupId());
            softly.assertThat(result.getMenuProducts()).isEqualTo(newMenu.getMenuProducts());
        });
    }

    @DisplayName("메뉴 금액이 음수이면 메뉴를 생성할 수 없습니다.")
    @Test
    void create_fail_menu_price_under_0() {
        // given
        final Menu wrongMenu = new Menu();
        wrongMenu.setId(1L);
        wrongMenu.setName("새 메뉴");
        wrongMenu.setPrice(new BigDecimal(-1000));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(wrongMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 금액이 null이면 메뉴를 생성할 수 없습니다.")
    @Test
    void create_fail_with_menu_price_null() {
        // given
        final Menu wrongMenu = new Menu();
        wrongMenu.setId(1L);
        wrongMenu.setName("새 메뉴");
        wrongMenu.setPrice(null);

        // when
        // then
        assertThatThrownBy(() -> menuService.create(wrongMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹에 포함되어 있지 않은 메뉴이면 메뉴를 생성할 수 없다.")
    @Test
    void create_fail_menu_not_contained_menuGroup() {
        // given
        final Menu wrongMenu = new Menu();
        wrongMenu.setId(1L);
        wrongMenu.setName("새 메뉴");
        wrongMenu.setPrice(new BigDecimal(1000));
        wrongMenu.setMenuGroupId(null);

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
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("메뉴 그룹");

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(2);
        menuProduct.setSeq(1L);
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        final Menu wrongMenu = new Menu();
        wrongMenu.setId(1L);
        wrongMenu.setName("새 메뉴");
        wrongMenu.setPrice(new BigDecimal(1000));
        wrongMenu.setMenuGroupId(menuGroup.getId());
        wrongMenu.setMenuProducts(menuProducts);

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
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("메뉴 그룹");

        final Product product = new Product();
        product.setId(1L);
        product.setName("상품1");
        product.setPrice(new BigDecimal(10000));

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menuProduct.setSeq(1L);
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        final Menu wrongMenu = new Menu();
        wrongMenu.setId(1L);
        wrongMenu.setName("새 메뉴");
        wrongMenu.setPrice(new BigDecimal(50000));
        wrongMenu.setMenuGroupId(menuGroup.getId());
        wrongMenu.setMenuProducts(menuProducts);

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
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(2);
        menuProduct1.setSeq(1L);

        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(2L);
        menuProduct2.setQuantity(1);
        menuProduct2.setSeq(2L);

        final Menu menu1 = new Menu();
        menu1.setId(1L);
        menu1.setName("메뉴1");
        menu1.setPrice(new BigDecimal(10000));
        menu1.setMenuGroupId(1L);
        menu1.setMenuProducts(List.of(menuProduct1));

        final Menu menu2 = new Menu();
        menu2.setId(1L);
        menu2.setName("메뉴2");
        menu2.setPrice(new BigDecimal(50000));
        menu2.setMenuGroupId(2L);
        menu2.setMenuProducts(List.of(menuProduct2));

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
