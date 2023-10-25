package kitchenpos.integration.fixture;

import static io.restassured.RestAssured.given;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import org.springframework.http.MediaType;
import java.util.List;

public class MenuGroupAPIFixture {

    public static final String DEFAULT_MENU_GROUP_NAME = "menuGroup";

    public static ExtractableResponse<Response> createMenuGroups(final MenuGroupCreateRequest request) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(request)
                .post("/api/menu-groups")
                .then()
                .log().all()
                .extract();
    }

    public static MenuGroupResponse createMenuGroupAndReturnResponse(final MenuGroupCreateRequest request) {
        return createMenuGroups(request).as(MenuGroupResponse.class);
    }

    public static MenuGroupResponse createDefaultMenuGroup() {
        return createMenuGroupAndReturnResponse(new MenuGroupCreateRequest(DEFAULT_MENU_GROUP_NAME));
    }

    public static List<MenuGroupResponse> listMenuGroups() {
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/menu-groups")
                .then()
                .log().all()
                .extract();
        return response.as(new TypeRef<>() {
        });
    }
}
