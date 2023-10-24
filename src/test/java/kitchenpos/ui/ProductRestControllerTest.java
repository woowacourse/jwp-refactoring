package kitchenpos.ui;

import io.restassured.RestAssured;
import kitchenpos.common.controller.ControllerTest;
import kitchenpos.ui.dto.ProductCreateRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class ProductRestControllerTest extends ControllerTest {

    @Test
    void Product를_생성하면_201을_반환한다() {
        // given
        final var 요청_준비 = RestAssured.given()
                .body(new ProductCreateRequest("디노 짬뽕", new BigDecimal(12000)))
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .post("/api/products");

        // then
        응답.then().assertThat().statusCode(CREATED.value());
    }

    @Test
    void Product를_조회하면_200을_반환한다() {
        // given
        final var 요청_준비 = RestAssured.given()
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .get("/api/products");

        // then
        응답.then().assertThat().statusCode(OK.value());
    }
}
