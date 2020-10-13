package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuProductDao menuProductDao;

    private MenuGroup savedMenuGroup;
    private Product savedProduct1;
    private Product savedProduct2;

    @BeforeEach
    void setUp() {
        this.savedMenuGroup = createSavedMenuGroup("두마리메뉴");
        this.savedProduct1 = createSavedProduct("양념치킨", BigDecimal.valueOf(16_000));
        this.savedProduct2 = createSavedProduct("간장치킨", BigDecimal.valueOf(16_000));
    }

    @DisplayName("새로운 메뉴 생성")
    @Test
    void createMenuTest() {
        Menu savedMenu = createSavedMenu("양념간장두마리메뉴", BigDecimal.valueOf(28_000), this.savedMenuGroup.getId(),
                                         new ArrayList<>());
        MenuProduct savedMenuProduct1 = createSavedMenuProduct(savedMenu.getId(), savedProduct1.getId(), 1);
        MenuProduct savedMenuProduct2 = createSavedMenuProduct(savedMenu.getId(), savedProduct2.getId(), 1);
        List<MenuProduct> savedMenuProducts = Arrays.asList(savedMenuProduct1, savedMenuProduct2);
        savedMenu.setMenuProducts(savedMenuProducts);

        Menu actualSavedMenu = this.menuService.create(savedMenu);

        assertAll(
                () -> assertThat(actualSavedMenu).isNotNull(),
                () -> assertThat(actualSavedMenu.getName()).isEqualTo(savedMenu.getName()),
                () -> assertThat(actualSavedMenu.getPrice()).isEqualTo(savedMenu.getPrice()),
                () -> assertThat(actualSavedMenu.getMenuGroupId()).isEqualTo(savedMenu.getMenuGroupId()),
                () -> assertThat(actualSavedMenu.getMenuProducts().size()).isEqualTo(savedMenu.getMenuProducts().size())
        );
    }

    @DisplayName("새로운 메뉴를 생성할 때 가격이 존재하지 않으면 예외 발생")
    @Test
    void createMenuWithNullPriceThenThrowException() {
        Menu menu = createMenuDomain("양념간장두마리메뉴", null, this.savedMenuGroup.getId(), new ArrayList<>());

        assertThatThrownBy(() -> this.menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 가격이 0 미만이면 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -9999})
    void createMenuWithInvalidPriceThenThrowException(int invalidPrice) {
        Menu menu = createMenuDomain("양념간장두마리메뉴", BigDecimal.valueOf(invalidPrice), this.savedMenuGroup.getId(),
                                     new ArrayList<>());

        assertThatThrownBy(() -> this.menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 존재하지 않는 메뉴 그룹을 지정하면 예외 발생")
    @Test
    void createMenuWithNotExistMenuGroupThenThrowException() {
        long notExistMenuGroupId = -1L;
        Menu menu = createMenuDomain("양념간장두마리메뉴", BigDecimal.valueOf(28_000), notExistMenuGroupId, new ArrayList<>());

        assertThatThrownBy(() -> this.menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 존재하지 않는 상품을 지정하면 예외 발생")
    @Test
    void createMenuWithNotExistMenuProductThenThrowException() {
        Menu menu = createSavedMenu("양념간장두마리메뉴", BigDecimal.valueOf(28_000), this.savedMenuGroup.getId(),
                                    new ArrayList<>());
        long notExistProductId = -1L;
        MenuProduct menuProduct = createMenuProductDomain(menu.getId(), notExistProductId, 1);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> this.menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 메뉴의 가격이 지정한 상품 가격의 총합을 초과하면 예외 발생")
    @Test
    void createMenuWithInvalidPriceThenThrowException() {
        BigDecimal invalidPrice = BigDecimal.valueOf(33_000);
        Menu savedMenu = createSavedMenu("양념간장두마리메뉴", invalidPrice, this.savedMenuGroup.getId(), new ArrayList<>());
        Product savedProduct1 = createSavedProduct("양념치킨", BigDecimal.valueOf(16_000));
        MenuProduct savedMenuProduct1 = createSavedMenuProduct(savedMenu.getId(), savedProduct1.getId(), 1);
        Product savedProduct2 = createSavedProduct("간장치킨", BigDecimal.valueOf(16_000));
        MenuProduct savedMenuProduct2 = createSavedMenuProduct(savedMenu.getId(), savedProduct2.getId(), 1);
        List<MenuProduct> savedMenuProducts = Arrays.asList(savedMenuProduct1, savedMenuProduct2);
        savedMenu.setMenuProducts(savedMenuProducts);

        assertThatThrownBy(() -> this.menuService.create(savedMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    private MenuGroup createSavedMenuGroup(String menuName) {
        MenuGroup menuGroup = createMenuGroupDomain(menuName);
        return this.menuGroupDao.save(menuGroup);
    }

    private Menu createSavedMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = createMenuDomain(name, price, menuGroupId, menuProducts);
        return this.menuDao.save(menu);
    }

    private Product createSavedProduct(String name, BigDecimal price) {
        Product product = createProductDomain(name, price);
        return this.productDao.save(product);
    }

    private MenuProduct createSavedMenuProduct(Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = createMenuProductDomain(menuId, productId, quantity);
        return this.menuProductDao.save(menuProduct);
    }

    private Menu createMenuDomain(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    private MenuGroup createMenuGroupDomain(String menuName) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuName);

        return menuGroup;
    }

    private Product createProductDomain(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    private MenuProduct createMenuProductDomain(Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }
}