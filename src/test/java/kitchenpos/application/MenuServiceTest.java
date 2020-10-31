package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class MenuServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuService menuService;

    private Product 후라이드치킨;
    private Product 프랜치프라이;
    private MenuGroup 세트메뉴;

    @BeforeEach
    void setUp() {
        ProductRequest 후라이드치킨_request = new ProductRequest("후라이드치킨", new BigDecimal(10_000));
        후라이드치킨 = productService.create(후라이드치킨_request);

        ProductRequest 프랜치프라이_request = new ProductRequest("프랜치프라이",new BigDecimal(5_000));
        프랜치프라이 = productService.create(프랜치프라이_request);

        세트메뉴 = new MenuGroup();
        세트메뉴.setName("세트메뉴");
        세트메뉴 = menuGroupService.create(세트메뉴);
    }

    @Test
    @DisplayName("create")
    void create() {
        Menu menu = new Menu();

        menu.setName("후라이드 세트");
        menu.setPrice(new BigDecimal(13_000));
        menu.setMenuGroupId(세트메뉴.getId());
        menu.setMenuProducts(createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이)));

        Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo("후라이드 세트");
        assertThat(savedMenu.getPrice().intValue()).isEqualTo(13_000);
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(세트메뉴.getId());
        assertThat(savedMenu.getMenuProducts()).hasSize(2);
    }

    @Test
    @DisplayName("create - price 가 null 일 때 예외처리")
    void create_IfPriceIsNull_ThrowException() {
        Menu menu = new Menu();

        menu.setName("후라이드 세트");
        menu.setMenuGroupId(세트메뉴.getId());
        menu.setMenuProducts(createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이)));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - price 가 음수일 때 예외처리")
    void create_IfPriceIsNegative_ThrowException() {
        Menu menu = new Menu();

        menu.setName("후라이드 세트");
        menu.setPrice(new BigDecimal(-13_000));
        menu.setMenuGroupId(세트메뉴.getId());
        menu.setMenuProducts(createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이)));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - 존재하지 않는 메뉴그룹 사용 예외처리")
    void create_IfMenuGroupIsNotExists_ThrowException() {
        Menu menu = new Menu();

        menu.setName("후라이드 세트");
        menu.setPrice(new BigDecimal(-13_000));
        menu.setMenuGroupId(500L);
        menu.setMenuProducts(createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이)));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - 존재하지 않는 product 를 포함하는 경우 예외처리")
    void create_IfProductNotExists_ThrowException() {
        Menu menu = new Menu();

        menu.setName("후라이드 세트");
        menu.setPrice(new BigDecimal(-13_000));
        menu.setMenuGroupId(500L);

        MenuProduct notExistProduct = new MenuProduct();
        notExistProduct.setProductId(500L);    // not existing product id
        notExistProduct.setQuantity(1);

        menu.setMenuProducts(Collections.singletonList(notExistProduct));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("create - 메뉴의 수량 값이 비정상적인 경우 예외처리")
    void create_IfMenuProductQuantityIsIllegal_ThrowException(int quantity) {
        Menu menu = new Menu();

        menu.setName("후라이드 세트");
        menu.setPrice(new BigDecimal(-13_000));
        menu.setMenuGroupId(500L);

        MenuProduct menuProductWithIllegalQuantity = new MenuProduct();
        menuProductWithIllegalQuantity.setProductId(후라이드치킨.getId());
        menuProductWithIllegalQuantity.setQuantity(quantity);

        menu.setMenuProducts(Collections.singletonList(menuProductWithIllegalQuantity));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - 메뉴 가격이 상품가격 총합보다 클 때 예외처리")
    void create_IfPriceIsLargerThenSumOfProductPrices_ThrowException() {
        Menu menu = new Menu();

        menu.setName("후라이드 세트");
        menu.setPrice(new BigDecimal(15_001));
        menu.setMenuGroupId(세트메뉴.getId());
        menu.setMenuProducts(createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이)));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        assertThat(menuService.list()).hasSize(0);

        // given
        Menu menu = new Menu();

        menu.setName("후라이드 세트");
        menu.setPrice(new BigDecimal(13_000));
        menu.setMenuGroupId(세트메뉴.getId());

        List<MenuProduct> menuProducts = createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이));

        menu.setMenuProducts(menuProducts);

        Menu savedMenu = menuService.create(menu);

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
        assertThat(menus.get(0).getId()).isEqualTo(savedMenu.getId());
    }

    private List<MenuProduct> createMenuProductsWithAllQuantityAsOne(List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (Product product : products) {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(product.getId());
            menuProduct.setQuantity(1);

            menuProducts.add(menuProduct);
        }
        return Collections.unmodifiableList(menuProducts);
    }
}
