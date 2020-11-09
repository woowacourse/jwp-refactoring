package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();

        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(10_000L));
        product1.setName("후라이드 치킨");

        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(20_000L));
        product2.setName("양념 치킨");

        product3.setId(3L);
        product3.setPrice(BigDecimal.valueOf(5_000L));
        product3.setName("시원한 아이스 아메리카노");

        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        MenuProduct menuProduct3 = new MenuProduct();

        menuProduct1.setProductId(product1.getId());
        menuProduct1.setQuantity(2L);

        menuProduct2.setProductId(product2.getId());
        menuProduct2.setQuantity(1L);

        menuProduct3.setProductId(product3.getId());
        menuProduct3.setQuantity(1L);

        Menu menu = new Menu();

        menu.setMenuGroupId(1L);
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(45_000L));
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2, menuProduct3));

        Menu savedMenu = new Menu();

        savedMenu.setId(1L);
        savedMenu.setMenuGroupId(menu.getMenuGroupId());
        savedMenu.setName(menu.getName());
        savedMenu.setPrice(menu.getPrice());
        savedMenu.setMenuProducts(menu.getMenuProducts());

        MenuProduct savedMenuProduct1 = new MenuProduct();
        MenuProduct savedMenuProduct2 = new MenuProduct();
        MenuProduct savedMenuProduct3 = new MenuProduct();

        savedMenuProduct1.setProductId(menuProduct1.getProductId());
        savedMenuProduct1.setQuantity(menuProduct1.getQuantity());
        savedMenuProduct1.setMenuId(savedMenu.getId());
        savedMenuProduct1.setSeq(1L);

        savedMenuProduct2.setProductId(menuProduct2.getProductId());
        savedMenuProduct2.setQuantity(menuProduct2.getQuantity());
        savedMenuProduct2.setMenuId(savedMenu.getId());
        savedMenuProduct2.setSeq(2L);

        savedMenuProduct3.setProductId(menuProduct3.getProductId());
        savedMenuProduct3.setQuantity(menuProduct3.getQuantity());
        savedMenuProduct3.setMenuId(savedMenu.getId());
        savedMenuProduct3.setSeq(3L);

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(menuProduct1.getProductId())).willReturn(Optional.of(product1));
        given(productDao.findById(menuProduct2.getProductId())).willReturn(Optional.of(product2));
        given(productDao.findById(menuProduct3.getProductId())).willReturn(Optional.of(product3));
        given(menuDao.save(any(Menu.class))).willReturn(savedMenu);
        given(menuProductDao.save(menuProduct1)).willReturn(savedMenuProduct1);
        given(menuProductDao.save(menuProduct2)).willReturn(savedMenuProduct2);
        given(menuProductDao.save(menuProduct3)).willReturn(savedMenuProduct3);

        Menu expected = menuService.create(menu);

        assertAll(
            () -> assertThat(expected).extracting(Menu::getId).isEqualTo(savedMenu.getId()),
            () -> assertThat(expected).extracting(Menu::getName).isEqualTo(savedMenu.getName()),
            () -> assertThat(expected).extracting(Menu::getPrice).isEqualTo(savedMenu.getPrice()),
            () -> assertThat(expected.getMenuProducts()).extracting(MenuProduct::getMenuId)
                .containsOnly(savedMenu.getId())
        );
    }

    @DisplayName("메뉴를 추가할 시 가격이 null일 경우 예외 처리한다.")
    @Test
    void createWithNullPrice() {
        Menu menu = new Menu();
        menu.setPrice(null);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 추가할 시 가격이 음수일 경우 예외 처리한다.")
    @Test
    void createWithNegativePrice() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-10L));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 추가할 시 존재하지 않는 MenuGroupId일 경우 예외 처리한다.")
    @Test
    void createWithNotExistingMenuGroupId() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1_000L));
        menu.setMenuGroupId(1L);

        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 추가할 시 존재하지 않는 ProductId일 경우 예외 처리한다.")
    @Test
    void createWithNotExistingProductId() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);

        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1_000L));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 추가할 시 메뉴 상품 가격의 총합이 메뉴의 가격보다 작을 경우 예외 처리한다.")
    @Test
    void createWith() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();

        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(10_000L));
        product1.setName("후라이드 치킨");

        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(20_000L));
        product2.setName("양념 치킨");

        product3.setId(3L);
        product3.setPrice(BigDecimal.valueOf(5_000L));
        product3.setName("시원한 아이스 아메리카노");

        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        MenuProduct menuProduct3 = new MenuProduct();

        menuProduct1.setProductId(product1.getId());
        menuProduct1.setQuantity(2L);

        menuProduct2.setProductId(product2.getId());
        menuProduct2.setQuantity(1L);

        menuProduct3.setProductId(product3.getId());
        menuProduct3.setQuantity(1L);

        Menu menu = new Menu();

        menu.setMenuGroupId(1L);
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(100_000L));
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2, menuProduct3));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(menuProduct1.getProductId())).willReturn(Optional.of(product1));
        given(productDao.findById(menuProduct2.getProductId())).willReturn(Optional.of(product2));
        given(productDao.findById(menuProduct3.getProductId())).willReturn(Optional.of(product3));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 전체 목록을 조회한다.")
    @Test
    void list() {
        Menu menu1 = new Menu();
        menu1.setId(1L);
        Menu menu2 = new Menu();
        menu2.setId(2L);
        List<Menu> menus = Arrays.asList(menu1, menu2);

        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();

        given(menuProductDao.findAllByMenuId(menu1.getId())).willReturn(Collections.singletonList(menuProduct1));
        given(menuProductDao.findAllByMenuId(menu2.getId())).willReturn(Collections.singletonList(menuProduct2));
        given(menuDao.findAll()).willReturn(menus);

        List<Menu> expected = menuService.list();

        assertAll(
            () -> assertThat(expected).hasSize(2),
            () -> assertThat(expected).element(0).extracting(Menu::getMenuProducts, LIST).contains(menuProduct1),
            () -> assertThat(expected).element(1).extracting(Menu::getMenuProducts, LIST).contains(menuProduct2)
        );
    }
}