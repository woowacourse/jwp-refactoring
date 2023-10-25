package kitchenpos.integration.fixture;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import org.springframework.http.MediaType;

public class MenuAPIFixture {

    public static ExtractableResponse<Response> createMenu(final MenuCreateRequest request) {
        return given().log().all()
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
}
