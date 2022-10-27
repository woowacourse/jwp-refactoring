package kitchenpos.acceptance.fixture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.http.HttpStatus;

public class MenuStepDefinition {

    public static long 메뉴를_생성한다(
        final String name,
        final int price,
        final long menuGroupId,
        final List<Long> productIds,
        final int quantity) {

        List<MenuProduct> menuProducts = productIds.stream()
            .map(productId -> new MenuProduct(productId, quantity))
            .collect(Collectors.toList());

        Menu menu = new Menu(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);

        return RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(menu)
            .when().log().all()
            .post("/api/menus")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().jsonPath().getLong("id");
    }

    public static List<Menu> 메뉴를_조회한다() {
        return RestAssured.given().log().all()
            .when().log().all()
            .get("/api/menus")
            .then().log().all()
            .extract().body().jsonPath().getList(".", Menu.class);
    }
}
