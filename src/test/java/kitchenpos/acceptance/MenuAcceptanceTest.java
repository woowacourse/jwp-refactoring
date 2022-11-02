package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


public class MenuAcceptanceTest extends AcceptanceTest {

    public static Long createMenu(MenuRequest menu) {
        String location = RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(menu)
                .when().log().all()
                .post("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        return Long.parseLong(location.split("/api/menus/")[1]);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void findMenus() {
        Long menuGroupId = MenuGroupAcceptanceTest.createMenuGroup("라라 메뉴");

        Long productId1 = ProductAcceptanceTest.createProduct("후라이드", 9000);
        Long productId2 = ProductAcceptanceTest.createProduct("돼지국밥", 7000);
        Long productId3 = ProductAcceptanceTest.createProduct("피자", 12000);
        Long productId4 = ProductAcceptanceTest.createProduct("수육", 18000);

        MenuProductRequest menuProduct1 = new MenuProductRequest(productId1, 1);
        MenuProductRequest menuProduct2 = new MenuProductRequest(productId2, 1);
        MenuProductRequest menuProduct3 = new MenuProductRequest(productId3, 1);
        MenuProductRequest menuProduct4 = new MenuProductRequest(productId4, 1);

        Long menuId1 = createMenu(
                new MenuRequest("해장 세트", BigDecimal.valueOf(15_000), menuGroupId, List.of(menuProduct1, menuProduct2)));
        Long menuId2 = createMenu(
                new MenuRequest("아재 세트", BigDecimal.valueOf(13_000), menuGroupId, List.of(menuProduct3, menuProduct2)));
        Long menuId3 = createMenu(
                new MenuRequest("피자치킨 세트", BigDecimal.valueOf(12_000), menuGroupId,
                        List.of(menuProduct1, menuProduct3)));
        Long menuId4 = createMenu(
                new MenuRequest("국밥 수육 메뉴", BigDecimal.valueOf(27_000), menuGroupId,
                        List.of(menuProduct3, menuProduct4)));

        List<MenuResponse> menus = getMenus();

        assertThat(menus).extracting(MenuResponse::getId, MenuResponse::getName,
                        menu -> menu.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(menuId1, "해장 세트", 15000),
                        tuple(menuId2, "아재 세트", 13000),
                        tuple(menuId3, "피자치킨 세트", 12000),
                        tuple(menuId4, "국밥 수육 메뉴", 27000)
                );
    }

    private List<MenuResponse> getMenus() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", MenuResponse.class);
    }
}
