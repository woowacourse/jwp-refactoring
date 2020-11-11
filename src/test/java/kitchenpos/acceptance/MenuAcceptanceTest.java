package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MenuAcceptanceTest extends AcceptanceTest {

    private List<ProductResponse> products;
    private MenuGroupResponse 세트_메뉴;

    @BeforeEach
    void setUp() {
        super.setUp();

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
        MenuResponse response = createMenu("후라이드 세트", products, 16_000L, 세트_메뉴.getId());

        assertThat(response.getId()).isNotNull();
        assertThat(response.getMenuGroupId()).isEqualTo(세트_메뉴.getId());
        assertThat(response.getName()).isEqualTo("후라이드 세트");

        assertThatMenuContainsProducts(response, products);

        // 메뉴 목록 조회
        List<MenuResponse> menus = findMenus();
        doesMenuExistInMenus(response, menus);
    }

    private List<MenuResponse> findMenus() {
        return given()
            .when()
                .get("/api/menus")
            .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract()
                .jsonPath()
                .getList("", MenuResponse.class);
    }

    private boolean doesMenuExistInMenus(MenuResponse menu, List<MenuResponse> menus) {
        return menus.stream()
            .anyMatch(menuEntity -> menuEntity
                .getId()
                .equals(menu.getId()));
    }

    /**
     * Feature: 메뉴의 가격 정보가 누락된 채로 메뉴 등록을 시도한다.
     *
     * Given 상품들이 등록되어 있다, 메뉴 그룹이 등록되어 있다.
     *
     * When 가격 정보가 누락된 채로 메뉴 등록을 시도한다.
     * Then 500 에러 응답을 받는다.
     */
    @Test
    @DisplayName("메뉴 생성 - 메뉴의 가격 정보가 누락된 경우 예외처리")
    void sendMenuRequestWithoutPrice() {
        assertThatFailToCreateMenuWithoutPrice("후라이드 세트", products, 세트_메뉴.getId());
    }

    /**
     * Feature: 메뉴의 가격을 음수로 해서 메뉴 등록을 시도한다.
     *
     * Given 상품들이 등록되어 있다, 메뉴 그룹이 등록되어 있다.
     *
     * When 가격을 음수로 하여 메뉴 등록을 시도한다.
     * Then 500 에러 응답을 받는다.
     */
    @Test
    @DisplayName("메뉴 생성 - 메뉴의 가격 정보가 음수인 경우 예외처리")
    void sendMenuRequestWithNegativePrice() {
        assertThatFailToCreateMenuWithNegativePrice("후라이드 세트", products, -1_000L, 세트_메뉴.getId());
    }

    /**
     * Feature: 메뉴 그룹에 속하지 않는 메뉴를 생성 시도한다.
     *
     * Given 상품들이 등록되어 있다, 메뉴 그룹이 등록되어 있다.
     *
     * When 메뉴 그룹을 지정하지 않고 메뉴 생성을 요청한다.
     * Then 500 에러 응답을 받는다.
     */
    @Test
    @DisplayName("메뉴 생성 - 메뉴 그룹을 지정하지 않은 경우 예외처리")
    void sendMenuRequestWithoutGroupId() {
        assertThatFailToCreateMenuWithoutMenuGroupId("후라이드 세트", products, 1_000L);
    }

    private void assertThatMenuContainsProducts(MenuResponse menu, List<ProductResponse> products) {
        List<MenuProduct> responseMenuProducts = menu.getMenuProducts();

        for (ProductResponse product : products) {
            assertThat(doesMenuContainProduct(responseMenuProducts, product)).isTrue();
        }
    }

    private boolean doesMenuContainProduct(List<MenuProduct> menuProducts, ProductResponse product) {
        return menuProducts.stream()
            .anyMatch(menuProduct -> menuProduct.getProductId()
                .equals(product.getId()));
    }

    private void assertThatFailToCreateMenuWithoutPrice(String menuName, List<ProductResponse> products, Long menuGroupId) {
        Map<String, Object> body = new HashMap<>();

        body.put("name", menuName);
        body.put("menuGroupId", menuGroupId);

        List<Map> menuProducts = makeMenuProducts(products);
        body.put("menuProducts", menuProducts);

        sendCreateMenuRequestThatFails(body);
    }

    private void assertThatFailToCreateMenuWithNegativePrice(String menuName,
        List<ProductResponse> products, Long menuPrice, Long menuGroupId) {
        if (menuPrice >= 0L) {
            throw new IllegalArgumentException("menuPrice 가 음수가 아닙니다.\n"
                + "음수가 아닌 가격에 대한 정상적인 메뉴 생성 테스트는"
                + "createMenu 메서드를 사용하세요.");
        }
        Map<String, Object> body = new HashMap<>();

        body.put("name", menuName);
        body.put("menuGroupId", menuGroupId);
        body.put("price", menuPrice);

        List<Map> menuProducts = makeMenuProducts(products);
        body.put("menuProducts", menuProducts);

        sendCreateMenuRequestThatFails(body);
    }

    private void assertThatFailToCreateMenuWithoutMenuGroupId(String menuName,
        List<ProductResponse> products, Long menuPrice) {
        Map<String, Object> body = new HashMap<>();

        body.put("name", menuName);
        body.put("price", menuPrice);

        List<Map> menuProducts = makeMenuProducts(products);
        body.put("menuProducts", menuProducts);

        sendCreateMenuRequestThatFails(body);
    }

    private void sendCreateMenuRequestThatFails(Map<String, Object> body) {
        given()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .post("/api/menus")
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
