package kitchenpos.ui;

import io.restassured.RestAssured;
import kitchenpos.common.controller.ControllerTest;
import kitchenpos.ui.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.Test;

import static io.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class MenuGroupRestControllerTest extends ControllerTest {

    @Test
    void MenuGroup을_생성하면_201을_반환한다() {
        // given
        final var 요청_준비 = RestAssured.given()
                .body(new MenuGroupCreateRequest("디노 극락 메뉴"))
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .post("/api/menu-groups");

        // then
        응답.then().assertThat().statusCode(CREATED.value());
    }

    @Test
    void MenuGroup을_조회하면_200을_반환한다() {
        // given
        final var 요청_준비 = RestAssured.given()
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .get("/api/menu-groups");

        // then
        응답.then().assertThat().statusCode(OK.value());
    }
}
