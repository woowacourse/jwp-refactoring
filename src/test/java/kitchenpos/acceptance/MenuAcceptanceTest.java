package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.request.MenuCreateRequest;
import kitchenpos.application.request.MenuProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    void Menu를_생성한다() {
        final ExtractableResponse<Response> actual = menu_생성("후라이드양념치킨");

        assertThat(actual.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void Menu의_목록을_조회할_수_있다() {
        menu_생성("후라이드양념치킨");

        final ExtractableResponse<Response> actual = menu_목록조회();

        assertThat(actual.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> menu_생성(final String name) {
        final MenuCreateRequest request = new MenuCreateRequest(name, new BigDecimal(32000), 1L,
                List.of(new MenuProductRequest(1L, 1L), new MenuProductRequest(2L, 1L)));
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/menus")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> menu_목록조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/menus")
                .then().log().all()
                .extract();
    }
}
