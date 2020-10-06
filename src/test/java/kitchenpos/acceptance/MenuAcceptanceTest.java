package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MenuAcceptanceTest extends AcceptanceTest {
    private List<Product> products;
    private MenuGroup 세트_메뉴;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();

        products.add(createProduct("후라이드 치킨", 9_000));
        products.add(createProduct("감자튀김", 5_500));
        products.add(createProduct("사워 크림 소스", 500));
        products.add(createProduct("맥주 500cc", 4_000));

        세트_메뉴 = createMenuGroup("세트 메뉴");
    }

    /**
     * Feature: 메뉴를 관리한다.
     *
     * Given 상품들이 등록되어 있다, 메뉴 그룹이 등록되어 있다.
     *
     * When 메뉴를 등록한다. Then 메뉴가 등록된다.
     *
     * When 모든 메뉴 목록을 조회한다. Then 메뉴 목록이 조회된다.
     */
    @Test
    @DisplayName("메뉴를 관리한다.")
    void manageMenu() {
        // 메뉴 등록
        Menu response = createMenu("후라이드 세트", 16_000L, 세트_메뉴.getId());

        assertThat(response.getId()).isNotNull();
        assertThat(response.getMenuGroupId()).isEqualTo(세트_메뉴.getId());
        assertThat(response.getName()).isEqualTo("후라이드 세트");

        List<MenuProduct> responseMenuProducts = response.getMenuProducts();

        for (Product product : products) {
            assertThat(doesMenuContainProduct(responseMenuProducts, product)).isTrue();
        }
    }

    private Menu createMenu(String name, Long price, Long menuGroupId) {
        Map<String, Object> body = new HashMap<>();

        body.put("name", name);
        body.put("price", price);
        body.put("menuGroupId", menuGroupId);

        List<Map> menuProducts = makeMenuProducts(products);
        body.put("menuProducts", menuProducts);

        return sendCreateMenuRequest(body);
    }

    private List<Map> makeMenuProducts(List<Product> products) {
        List<Map> menuProducts = new ArrayList<>();

        for (Product product : products) {
            Map<String, String> menuProduct = new HashMap<>();

            menuProduct.put("productId", Long.toString(product.getId()));
            menuProduct.put("quantity", "1");

            menuProducts.add(menuProduct);
        }
        return Collections.unmodifiableList(menuProducts);
    }

    private Menu sendCreateMenuRequest(Map<String, Object> body) {
        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/api/menus")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Menu.class);
    }

    private boolean doesMenuContainProduct(List<MenuProduct> menuProducts, Product product) {
        return menuProducts.stream()
            .anyMatch(menuProduct -> menuProduct.getProductId()
                .equals(product.getId()));
    }
}
