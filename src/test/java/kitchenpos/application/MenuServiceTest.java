package kitchenpos.application;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.beans.factory.annotation.Autowired;

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
    @Autowired
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
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        menuGroup = createMenuGroup("Menu Group1");
        menuGroup.setId(1L);

        product1 = createProduct("product1", BigDecimal.valueOf(1000));
        product2 = createProduct("product2", BigDecimal.valueOf(1000));
        product1.setId(1L);
        product2.setId(2L);

        menuProduct1 = createMenuProduct(null, 1L, 1);
        menuProduct2 = createMenuProduct(null, 2L, 2);
    }

    @Test
    void create() {
        Menu menu = createMenu("menu", 1L, BigDecimal.valueOf(1000));
        menu.setId(1L);
        menuProduct1.setMenuId(1L);
        menuProduct2.setMenuId(1L);
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(java.util.Optional.ofNullable(product1));
        given(productDao.findById(2L)).willReturn(java.util.Optional.ofNullable(product2));
        given(menuDao.save(any())).willReturn(menu);
        given(menuProductDao.save(menuProduct1)).willReturn(menuProduct1);
        given(menuProductDao.save(menuProduct2)).willReturn(menuProduct2);

        Menu savedMenu = menuService.create(menu);

        assertAll(
            () -> assertThat(savedMenu.getId()).isEqualTo(menu.getId()),
            () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
            () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
            () -> assertThat(savedMenu.getPrice().toBigInteger()).isEqualTo(menu.getPrice().toBigInteger()),
            () -> assertThat(savedMenu.getMenuProducts().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("메뉴의 합이 각 상품의 합보다 작아야 한다.")
    void createFail() {
        Menu menu = createMenu("menu", 1L, BigDecimal.valueOf(6000));
        menu.setId(1L);
        menuProduct1.setMenuId(1L);
        menuProduct2.setMenuId(1L);
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(java.util.Optional.ofNullable(product1));
        given(productDao.findById(2L)).willReturn(java.util.Optional.ofNullable(product2));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴의 가격은 상품 가격의 합보다 작아야 합니다.");
    }

    @Test
    @DisplayName("메뉴의 목록을 불러올 수 있어야 한다.")
    void list() {
        Menu menu1 = createMenu("menu1", 1L, BigDecimal.valueOf(1000));
        Menu menu2 = createMenu("menu2", 1L, BigDecimal.valueOf(1000));
        menu1.setId(1L);
        menu2.setId(2L);
        menu1.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        menu2.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        List<Menu> menus = Arrays.asList(menu1, menu2);

        given(menuDao.findAll()).willReturn(menus);

        List<Menu> expectedMenus = menuService.list();

        assertThat(expectedMenus.size()).isEqualTo(menus.size());
    }
}
