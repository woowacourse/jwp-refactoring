package kitchenpos.support;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.request.CreateMenuRequest;

public class TestMenuFixture {

    public static Long 메뉴_수정(Long menuId, CreateMenuRequest request) {
        ExtractableResponse<Response> response = RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .when().put("/api/menus/{menuId}", menuId)
                .then().log().all()
                .statusCode(200)
                .extract();
        return menuId;
    }
}
