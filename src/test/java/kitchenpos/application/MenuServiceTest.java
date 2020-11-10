package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
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

    private ProductResponse 후라이드치킨;
    private ProductResponse 프랜치프라이;
    private MenuGroup 세트메뉴;

    @BeforeEach
    void setUp() {
        ProductRequest 후라이드치킨_request = new ProductRequest("후라이드치킨", BigDecimal.valueOf(10_000));
        후라이드치킨 = productService.create(후라이드치킨_request);

        ProductRequest 프랜치프라이_request = new ProductRequest("프랜치프라이", BigDecimal.valueOf(5_000));
        프랜치프라이 = productService.create(프랜치프라이_request);

        MenuGroupRequest 세트메뉴_request = new MenuGroupRequest("세트메뉴");
        세트메뉴 = menuGroupService.create(세트메뉴_request);
    }

    @Test
    @DisplayName("create")
    void create() {
        List<MenuProductRequest> menuProducts = createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이));

        MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(13_000),
            세트메뉴.getId(), menuProducts);
        MenuResponse savedMenu = menuService.create(menuRequest);

        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo("후라이드 세트");
        assertThat(savedMenu.getPrice().intValue()).isEqualTo(13_000);
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(세트메뉴.getId());
        assertThat(savedMenu.getMenuProducts()).hasSize(2);
    }

    @Test
    @DisplayName("create - price 가 null 일 때 예외처리")
    void create_IfPriceIsNull_ThrowException() {
        List<MenuProductRequest> menuProducts = createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이));

        MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(-13_000),
            세트메뉴.getId(), menuProducts);

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - price 가 음수일 때 예외처리")
    void create_IfPriceIsNegative_ThrowException() {
        List<MenuProductRequest> menuProducts = createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이));
        MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(-13_000),
            세트메뉴.getId(), menuProducts);

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - 존재하지 않는 메뉴그룹 사용 예외처리")
    void create_IfMenuGroupIsNotExists_ThrowException() {
        List<MenuProductRequest> menuProducts = createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이));
        MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(-13_000), 500L,
            menuProducts);

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - 존재하지 않는 product 를 포함하는 경우 예외처리")
    void create_IfProductNotExists_ThrowException() {
        //500L은 존재하지 않는 productId를 뜻함
        MenuProductRequest notExistProduct = new MenuProductRequest(500L, 1);

        MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(-13_000), 500L,
            Collections.singletonList(notExistProduct));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("create - 메뉴의 수량 값이 비정상적인 경우 예외처리")
    void create_IfMenuProductQuantityIsIllegal_ThrowException(int quantity) {
        MenuProductRequest menuProductWithIllegalQuantity = new MenuProductRequest(후라이드치킨.getId(), quantity);

        MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(-13_000), 500L,
            Collections.singletonList(menuProductWithIllegalQuantity));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - 메뉴 가격이 상품가격 총합보다 클 때 예외처리")
    void create_IfPriceIsLargerThenSumOfProductPrices_ThrowException() {
        List<MenuProductRequest> menuProducts = createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이));
        MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(15_001),
            세트메뉴.getId(), menuProducts);

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        assertThat(menuService.list()).hasSize(0);

        // given
        List<MenuProductRequest> menuProducts = createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이));

        MenuRequest request = new MenuRequest(
            "후라이드 세트", BigDecimal.valueOf(13_000), 세트메뉴.getId(), menuProducts);

        MenuResponse savedMenu = menuService.create(request);

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
        assertThat(menus.get(0).getId()).isEqualTo(savedMenu.getId());
    }

    private List<MenuProductRequest> createMenuProductsWithAllQuantityAsOne(List<ProductResponse> products) {
        List<MenuProductRequest> menuProducts = new ArrayList<>();

        for (ProductResponse product : products) {
            MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 1);
            menuProducts.add(menuProduct);
        }
        return Collections.unmodifiableList(menuProducts);
    }
}
