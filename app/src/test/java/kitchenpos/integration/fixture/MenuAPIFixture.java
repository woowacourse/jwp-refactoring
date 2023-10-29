package kitchenpos.integration.fixture;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.response.MenuResponse;
import org.springframework.http.MediaType;
import java.math.BigDecimal;
import java.util.List;

public class MenuAPIFixture {

    public static final long DEFAULT_MENU_QUANTITY = 2L;
    public static final String DEFAULT_MENU_NAME = "menu";

    public static ExtractableResponse<Response> createMenu(final MenuCreateRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(request)
                .post("/api/menus")
                .then()
                .log().all()
                .extract();
    }

    public static MenuResponse createMenuAndReturnResponse(final MenuCreateRequest request) {
        return createMenu(request).as(MenuResponse.class);
    }

    public static MenuResponse createDefaultMenu(final Long productId, final BigDecimal productPrice, final Long menuGroupId) {
        final MenuProductRequest menuProductRequest = new MenuProductRequest(productId, DEFAULT_MENU_QUANTITY);
        final BigDecimal price = productPrice.multiply(BigDecimal.valueOf(DEFAULT_MENU_QUANTITY));
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(DEFAULT_MENU_NAME, price, menuGroupId, List.of(menuProductRequest));
        return createMenuAndReturnResponse(menuCreateRequest);
    }

    public static List<MenuResponse> listMenus() {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/menus")
                .then()
                .log().all()
                .extract();
        return response.as(new TypeRef<>() {
        });
    }
}
