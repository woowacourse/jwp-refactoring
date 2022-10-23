package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.request.MenuGroupCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void MenuGroup을_생성한다() {
        final ExtractableResponse<Response> actual = menuGroup_생성("반반메뉴");

        assertThat(actual.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void MenuGroup의_목록을_조회할_수_있다() {
        menuGroup_생성("반반메뉴");

        final ExtractableResponse<Response> actual = menuGroup_목록조회();

        assertThat(actual.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> menuGroup_생성(final String name) {
        final MenuGroupCreateRequest request = new MenuGroupCreateRequest(name);
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> menuGroup_목록조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/menu-groups")
                .then().log().all()
                .extract();
    }
}
