package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuService menuService;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;
    private MenuGroup menuGroup;
    private Product product1;
    private Product product2;
    private List<MenuProduct> menuProducts;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;
    private Menu menu;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("Menu Group1");

        menu = new Menu();
        menu.setId(1L);
        menu.setMenuGroupId(1L);
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(2000));

        product1 = new Product();
        product2 = new Product();
        product1.setId(1L);
        product2.setId(2L);
        product1.setPrice(BigDecimal.valueOf(1000));
        product2.setPrice(BigDecimal.valueOf(1000));
        product1.setName("product1");
        product2.setName("product2");

        menuProduct1 = new MenuProduct();
        menuProduct2 = new MenuProduct();
        menuProduct1.setQuantity(1);
        menuProduct2.setQuantity(2);
        menuProduct1.setProductId(1L);
        menuProduct2.setProductId(2L);
        menuProduct1.setMenuId(1L);
        menuProduct2.setMenuId(2L);
        menuProducts = Arrays.asList(menuProduct1, menuProduct2);
    }

    @Test
    void create() {
        menu.setMenuProducts(menuProducts);

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(java.util.Optional.ofNullable(product1));
        given(productDao.findById(2L)).willReturn(java.util.Optional.ofNullable(product2));
        given(menuDao.save(any())).willReturn(menu);
        given(menuProductDao.save(menuProduct1)).willReturn(menuProduct1);
        given(menuProductDao.save(menuProduct2)).willReturn(menuProduct2);

        Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isEqualTo(menu.getId());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
        assertThat(savedMenu.getPrice().toBigInteger()).isEqualTo(menu.getPrice().toBigInteger());
        assertThat(savedMenu.getMenuProducts().size()).isEqualTo(menuProducts.size());
        assertThat(savedMenu.getMenuProducts().get(0)).isEqualTo(menuProduct1);
        assertThat(savedMenu.getMenuProducts().get(1)).isEqualTo(menuProduct2);
    }

    @Test
    @DisplayName("메뉴의 합이 각 상품의 합보다 작아야 한다.")
    void createFail() {
        menu.setPrice(BigDecimal.valueOf(6000));
        menu.setMenuProducts(menuProducts);

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(java.util.Optional.ofNullable(product1));
        given(productDao.findById(2L)).willReturn(java.util.Optional.ofNullable(product2));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴의 가격은 상품 가격의 합보다 작아야 합니다.");
    }

    @Test
    void list() {
        Menu newMenu = new Menu();
        newMenu.setId(2L);
        newMenu.setMenuProducts(menuProducts);
        newMenu.setPrice(BigDecimal.valueOf(1000));
        newMenu.setName("newMenu");
        newMenu.setMenuGroupId(1L);
        List<Menu> menus = Arrays.asList(menu, newMenu);

        given(menuDao.findAll()).willReturn(menus);

        List<Menu> foundMenus = menuService.list();
        assertThat(foundMenus.size()).isEqualTo(menus.size());
        assertThat(foundMenus.get(0)).isEqualTo(menus.get(0));
        assertThat(foundMenus.get(1)).isEqualTo(menus.get(1));
    }
}
