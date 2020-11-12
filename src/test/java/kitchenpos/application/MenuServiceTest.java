package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ServiceTest
class MenuServiceTest {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();

        product1.setPrice(BigDecimal.valueOf(10_000L));
        product1.setName("후라이드 치킨");

        product2.setPrice(BigDecimal.valueOf(20_000L));
        product2.setName("양념 치킨");

        product3.setPrice(BigDecimal.valueOf(5_000L));
        product3.setName("시원한 아이스 아메리카노");

        Product savedProduct1 = productDao.save(product1);
        Product savedProduct2 = productDao.save(product2);
        Product savedProduct3 = productDao.save(product3);

        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        MenuProduct menuProduct3 = new MenuProduct();

        menuProduct1.setProductId(savedProduct1.getId());
        menuProduct1.setQuantity(2L);

        menuProduct2.setProductId(savedProduct2.getId());
        menuProduct2.setQuantity(1L);

        menuProduct3.setProductId(savedProduct3.getId());
        menuProduct3.setQuantity(1L);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test_group");

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();

        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(45_000L));
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2, menuProduct3));

        Menu actual = menuService.create(menu);

        assertAll(
            () -> assertThat(actual).extracting(Menu::getId).isNotNull(),
            () -> assertThat(actual).extracting(Menu::getName).isEqualTo(menu.getName()),
            () -> assertThat(actual).extracting(Menu::getPrice, BIG_DECIMAL).isEqualByComparingTo(menu.getPrice()),
            () -> assertThat(actual.getMenuProducts()).extracting(MenuProduct::getMenuId)
                .containsOnly(actual.getId())
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
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        menu.setMenuGroupId(savedMenuGroup.getId());

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 추가할 시 메뉴 상품 가격의 총합이 메뉴의 가격보다 작을 경우 예외 처리한다.")
    @Test
    void createWithUnderTotalPrice() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();

        product1.setPrice(BigDecimal.valueOf(10_000L));
        product1.setName("후라이드 치킨");

        product2.setPrice(BigDecimal.valueOf(20_000L));
        product2.setName("양념 치킨");

        product3.setPrice(BigDecimal.valueOf(5_000L));
        product3.setName("시원한 아이스 아메리카노");

        Product savedProduct1 = productDao.save(product1);
        Product savedProduct2 = productDao.save(product2);
        Product savedProduct3 = productDao.save(product3);

        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        MenuProduct menuProduct3 = new MenuProduct();

        menuProduct1.setProductId(savedProduct1.getId());
        menuProduct1.setQuantity(2L);

        menuProduct2.setProductId(savedProduct2.getId());
        menuProduct2.setQuantity(1L);

        menuProduct3.setProductId(savedProduct3.getId());
        menuProduct3.setQuantity(1L);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test");

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();

        menu.setMenuGroupId(1L);
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(100_000L));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2, menuProduct3));

        Menu savedMenu = menuDao.save(menu);

        menuProduct1.setMenuId(savedMenu.getId());
        menuProduct2.setMenuId(savedMenu.getId());
        menuProduct3.setMenuId(savedMenu.getId());

        menuProductDao.save(menuProduct1);
        menuProductDao.save(menuProduct2);
        menuProductDao.save(menuProduct3);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 전체 목록을 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test");

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Product product1 = new Product();
        product1.setName("test1");
        product1.setPrice(BigDecimal.valueOf(1_000L));

        Product product2 = new Product();
        product2.setName("test2");
        product2.setPrice(BigDecimal.valueOf(2_000L));

        Product savedProduct1 = productDao.save(product1);
        Product savedProduct2 = productDao.save(product2);

        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(savedProduct1.getId());
        menuProduct1.setQuantity(2);

        MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(savedProduct2.getId());
        menuProduct2.setQuantity(2);

        Menu menu1 = new Menu();
        menu1.setName("test1");
        menu1.setPrice(BigDecimal.valueOf(2_000L));
        menu1.setMenuProducts(Collections.singletonList(menuProduct1));
        menu1.setMenuGroupId(savedMenuGroup.getId());

        Menu menu2 = new Menu();
        menu2.setName("test2");
        menu2.setPrice(BigDecimal.valueOf(4_000L));
        menu2.setMenuProducts(Collections.singletonList(menuProduct2));
        menu2.setMenuGroupId(savedMenuGroup.getId());

        Menu savedMenu1 = menuDao.save(menu1);
        Menu savedMenu2 = menuDao.save(menu2);

        menuProduct1.setMenuId(savedMenu1.getId());
        menuProduct2.setMenuId(savedMenu2.getId());

        menuProductDao.save(menuProduct1);
        menuProductDao.save(menuProduct2);

        List<Menu> actual = menuService.list();

        assertAll(
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual).element(0).extracting(Menu::getMenuProducts, LIST).isNotEmpty(),
            () -> assertThat(actual).element(1).extracting(Menu::getMenuProducts, LIST).isNotEmpty()
        );
    }
}