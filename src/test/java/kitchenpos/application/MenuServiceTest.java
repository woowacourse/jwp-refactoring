package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
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
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup savedMenuGroup;
    private Product savedProduct1;
    private Product savedProduct2;
    private Product savedProduct3;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;
    private MenuProduct menuProduct3;

    @BeforeEach
    void setUp() {
        this.savedMenuGroup = createSavedMenuGroup("두마리메뉴");
        this.savedProduct1 = createSavedProduct("양념치킨", BigDecimal.valueOf(16_000));
        this.savedProduct2 = createSavedProduct("간장치킨", BigDecimal.valueOf(16_000));
        this.savedProduct3 = createSavedProduct("후라이드치킨", BigDecimal.valueOf(15_000));
        this.menuProduct1 = new MenuProduct(this.savedProduct1, 1);
        this.menuProduct2 = new MenuProduct(this.savedProduct2, 1);
        this.menuProduct3 = new MenuProduct(this.savedProduct3, 1);
    }

    @DisplayName("새로운 메뉴 생성")
    @Test
    void createMenuTest() {
        Menu menu = new Menu("양념간장두마리메뉴", BigDecimal.valueOf(28_000), this.savedMenuGroup);
        List<MenuProduct> menuProducts = Arrays.asList(this.menuProduct1, this.menuProduct2);
        menu.setMenuProducts(menuProducts);

        Menu savedMenu = this.menuService.create(menu);

        assertAll(
                () -> assertThat(savedMenu).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(savedMenu.getPrice().toBigInteger()).isEqualTo(menu.getPrice().toBigInteger()),
                () -> assertThat(savedMenu.getMenuGroup()).isEqualTo(menu.getMenuGroup()),
                () -> assertThat(savedMenu.getMenuProducts().size()).isEqualTo(menu.getMenuProducts().size())
        );
    }

    @DisplayName("새로운 메뉴를 생성할 때 가격이 존재하지 않으면 예외 발생")
    @Test
    void createMenuWithNullPriceThenThrowException() {
        Menu menu = new Menu("양념간장두마리메뉴", null, this.savedMenuGroup);

        assertThatThrownBy(() -> this.menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 가격이 0 미만이면 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -9999})
    void createMenuWithInvalidPriceThenThrowException(int invalidPrice) {
        Menu menu = new Menu("양념간장두마리메뉴", BigDecimal.valueOf(invalidPrice), this.savedMenuGroup);

        assertThatThrownBy(() -> this.menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 존재하지 않는 메뉴 그룹을 지정하면 예외 발생")
    @Test
    void createMenuWithNotExistMenuGroupThenThrowException() {
        MenuGroup notExistMenuGroup = new MenuGroup();
        long notExistMenuGroupId = -1L;
        notExistMenuGroup.setId(notExistMenuGroupId);
        Menu menu = new Menu("양념간장두마리메뉴", BigDecimal.valueOf(28_000), notExistMenuGroup);

        assertThatThrownBy(() -> this.menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 존재하지 않는 상품을 지정하면 예외 발생")
    @Test
    void createMenuWithNotExistMenuProductThenThrowException() {
        Menu menu = new Menu("양념간장두마리메뉴", BigDecimal.valueOf(28_000), this.savedMenuGroup);
        Product notExistProduct = new Product();
        long notExistProductId = -1L;
        notExistProduct.setId(notExistProductId);
        MenuProduct menuProduct = new MenuProduct(notExistProduct, 1);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> this.menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 메뉴의 가격이 지정한 상품 가격의 총합을 초과하면 예외 발생")
    @Test
    void createMenuWithInvalidPriceThenThrowException() {
        Product savedProduct1 = createSavedProduct("양념치킨", BigDecimal.valueOf(16_000));
        Product savedProduct2 = createSavedProduct("간장치킨", BigDecimal.valueOf(16_000));
        MenuProduct menuProduct1 = new MenuProduct(savedProduct1, 1);
        MenuProduct menuProduct2 = new MenuProduct(savedProduct2, 1);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        BigDecimal invalidPrice = BigDecimal.valueOf(33_000);
        Menu menu = new Menu("양념간장두마리메뉴", invalidPrice, this.savedMenuGroup);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> this.menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 모든 메뉴를 조회")
    @Test
    void listMenuTest() {
        Menu menu1 = new Menu("양념간장두마리메뉴", BigDecimal.valueOf(28_000), this.savedMenuGroup);
        List<MenuProduct> menuProducts1 = Arrays.asList(this.menuProduct1, this.menuProduct2);
        menu1.setMenuProducts(menuProducts1);

        Menu menu2 = new Menu("후라이드양념두마리메뉴", BigDecimal.valueOf(27_000), this.savedMenuGroup);
        List<MenuProduct> menuProducts2 = Arrays.asList(this.menuProduct1, this.menuProduct3);
        menu2.setMenuProducts(menuProducts2);

        List<Menu> menus = Arrays.asList(menu1, menu2);
        menus.forEach(menu -> this.menuService.create(menu));

        List<Menu> savedMenus = this.menuService.list();

        assertAll(
                () -> assertThat(savedMenus.size()).isEqualTo(menus.size()),
                () -> assertThat(savedMenus.contains(menu1)),
                () -> assertThat(savedMenus.contains(menu2))
        );
    }

    private MenuGroup createSavedMenuGroup(String menuName) {
        MenuGroup menuGroup = new MenuGroup(menuName);
        return this.menuGroupRepository.save(menuGroup);
    }

    private Product createSavedProduct(String name, BigDecimal price) {
        Product product = new Product(name, price);
        return this.productRepository.save(product);
    }
}