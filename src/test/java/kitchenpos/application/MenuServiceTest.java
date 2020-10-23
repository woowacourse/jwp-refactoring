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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.TestFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@Sql("/truncate.sql")
@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @DisplayName("새로운 Menu를 추가할 수 있다.")
    @ValueSource(ints = {30_000, 32_000})
    @ParameterizedTest
    void createMenuTest(int price) {
        Product product = createProduct("후라이드", BigDecimal.valueOf(16_000));
        Product savedProduct = productDao.save(product);

        MenuGroup tweChickenMenu = createMenuGroup("두마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(tweChickenMenu);

        MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 2L);
        Menu menu = createMenu("두마리 후라이드치킨", BigDecimal.valueOf(price), savedMenuGroup.getId(), menuProduct);

        Menu savedMenu = menuService.create(menu);

        assertAll(() -> {
            assertThat(savedMenu).isNotNull();
            assertThat(savedMenu.getName()).isEqualTo(menu.getName());
            assertThat(savedMenu.getPrice()).isEqualByComparingTo(menu.getPrice());
            assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            assertThat(savedMenu.getMenuProducts()).hasSize(1);
        });
    }

    @DisplayName("예외: 이름이 없는 Menu를 추가")
    @Test
    void createMenuWithoutNameTest() {
        Product product = createProduct("후라이드", BigDecimal.valueOf(16_000));
        Product savedProduct = productDao.save(product);

        MenuGroup tweChickenMenu = createMenuGroup("두마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(tweChickenMenu);

        MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 2L);
        Menu menu = createMenu(null, BigDecimal.valueOf(30_000), savedMenuGroup.getId(), menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("예외: 가격이 0보다 작은 Menu를 추가")
    @Test
    void createMenuWithoutPriceTest() {
        Product product = createProduct("후라이드", BigDecimal.valueOf(16_000));
        Product savedProduct = productDao.save(product);

        MenuGroup tweChickenMenu = createMenuGroup("두마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(tweChickenMenu);

        MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 2L);
        Menu menu = createMenu("두마리 후라이드치킨", BigDecimal.valueOf(-1), savedMenuGroup.getId(), menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 가격이 없는 Menu를 추가")
    @Test
    void createMenuWithNegativePriceTest() {
        Product product = createProduct("후라이드", BigDecimal.valueOf(16_000));
        Product savedProduct = productDao.save(product);

        MenuGroup tweChickenMenu = createMenuGroup("두마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(tweChickenMenu);

        MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 2L);
        Menu menu = createMenu("두마리 후라이드치킨", null, savedMenuGroup.getId(), menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 존재하지 않는 MenuGroupId로 Menu를 추가")
    @Test
    void createWithInvalidMenuGroupIdTest() {
        Product product = createProduct("후라이드", BigDecimal.valueOf(16_000));
        Product savedProduct = productDao.save(product);

        MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 2L);
        Menu menu = createMenu("두마리 후라이드치킨", BigDecimal.valueOf(30_000), 100L, menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: MenuGroupId가 null인 Menu를 추가")
    @Test
    void createWithoutMenuGroupIdTest() {
        Product product = createProduct("후라이드", BigDecimal.valueOf(16_000));
        Product savedProduct = productDao.save(product);

        MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 2L);
        Menu menu = createMenu("두마리 후라이드치킨", BigDecimal.valueOf(30_000), null, menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 존재하지 않는 ProductId로 MenuProduct로 Menu를 추가")
    @Test
    void createMenuWithInvalidProductIdTest() {
        MenuGroup tweChickenMenu = createMenuGroup("두마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(tweChickenMenu);

        MenuProduct menuProduct = createMenuProduct(100L, 2L);
        Menu menu = createMenu("두마리 후라이드치킨", BigDecimal.valueOf(30_000), savedMenuGroup.getId(), menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: ProductId가 null인 MenuProduct로 Menu를 추가")
    @Test
    void createMenuWithoutProductIdTest() {
        MenuGroup tweChickenMenu = createMenuGroup("두마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(tweChickenMenu);

        MenuProduct menuProduct = createMenuProduct(null, 2L);
        Menu menu = createMenu("두마리 후라이드치킨", BigDecimal.valueOf(30_000), savedMenuGroup.getId(), menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: MenuProducts에 null을 이용해 Menu를 추가")
    @Test
    void createMenuWithoutMenuProductsTest() {
        MenuGroup tweChickenMenu = createMenuGroup("두마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(tweChickenMenu);

        Menu menu = createMenu("두마리 후라이드치킨", BigDecimal.valueOf(30_000), savedMenuGroup.getId());
        menu.setMenuProducts(null);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("예외: MenuProducts에 EmptyList를 이용해 Menu를 추가")
    @Test
    void createMenuWithEmptyMenuProductsTest() {
        MenuGroup tweChickenMenu = createMenuGroup("두마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(tweChickenMenu);

        Menu menu = createMenu("두마리 후라이드치킨", BigDecimal.valueOf(30_000), savedMenuGroup.getId());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: Menu의 가격이 Menu에 포함된 MenuProduct 가격의 합보다 비싼 경우")
    @Test
    void createMenuWithWronglyCalculatedPriceTest() {
        Product product = createProduct("후라이드", BigDecimal.valueOf(16_000));
        Product savedProduct = productDao.save(product);

        MenuGroup tweChickenMenu = createMenuGroup("두마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(tweChickenMenu);

        MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 2L);
        Menu menu = createMenu("두마리 후라이드치킨", BigDecimal.valueOf(33_000), savedMenuGroup.getId(), menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 Menu를 조회하면 MenuProduct와 함께 조회할 수 있다.")
    @Test
    void findAllMenusTest() {
        Product product = createProduct("후라이드", BigDecimal.valueOf(16_000));
        Product savedProduct = productDao.save(product);

        MenuGroup tweChickenMenu = createMenuGroup("두마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(tweChickenMenu);

        Menu menu = createMenu("두마리 후라이드치킨", BigDecimal.valueOf(33_000), savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);

        MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 2L, savedMenu.getId());
        menuProductDao.save(menuProduct);

        List<Menu> menus = menuService.list();

        assertAll(() -> {
            assertThat(menus).hasSize(1);
            assertThat(menus).extracting(Menu::getName).containsOnly(savedMenu.getName());
            assertThat(menus).extracting(Menu::getMenuProducts).isNotEmpty();
        });
    }
}