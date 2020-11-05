package kitchenpos.application;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
@Sql("classpath:delete.sql")
class MenuServiceTest {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuProductDao menuProductDao;
    @Autowired
    private ProductDao productDao;

    private MenuGroup menuGroup;
    private Product product1;
    private Product product2;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        menuGroup = menuGroupDao.save(createMenuGroup("Menu Group1"));
        product1 = productDao.save(createProduct("product1", BigDecimal.valueOf(1000)));
        product2 = productDao.save(createProduct("product2", BigDecimal.valueOf(1000)));

        menuProduct1 = createMenuProduct(null, product1.getId(), 1);
        menuProduct2 = createMenuProduct(null, product2.getId(), 2);
    }

    @Test
    void create() {
        Menu menu = createMenu("menu", menuGroup.getId(), BigDecimal.valueOf(1000));
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        Menu savedMenu = menuService.create(menu);

        assertAll(
            () -> assertThat(savedMenu.getId()).isNotNull(),
            () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
            () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
            () -> assertThat(savedMenu.getPrice().toBigInteger()).isEqualTo(menu.getPrice().toBigInteger()),
            () -> assertThat(savedMenu.getMenuProducts().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("메뉴의 합이 각 상품의 합보다 작아야 한다.")
    void createFail() {
        Menu menu = createMenu("menu", menuGroup.getId(), BigDecimal.valueOf(6000));
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴의 가격은 상품 가격의 합보다 작아야 합니다.");
    }

    @Test
    @DisplayName("메뉴의 목록을 불러올 수 있어야 한다.")
    void list() {
        Menu menu1 = createMenu("menu1", menuGroup.getId(), BigDecimal.valueOf(1000));
        Menu menu2 = createMenu("menu2", menuGroup.getId(), BigDecimal.valueOf(1000));
        menu1.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        menu2.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        menuService.create(menu1);
        menuService.create(menu2);

        List<Menu> expectedMenus = menuService.list();

        assertThat(expectedMenus.size()).isEqualTo(2);
    }
}
