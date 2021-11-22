package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menugroup.service.MenuGroupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceTest extends AcceptanceTest {
    public static MenuGroupRequest 튀김류 = new MenuGroupRequest("튀김류");

    public static ExtractableResponse<Response> MENU_GROUP_생성_요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when()
                .post("/api/menu-groups")
                .then()
                .extract();
    }

    @Test
    void save() {
        ExtractableResponse<Response> response = MENU_GROUP_생성_요청(튀김류);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
