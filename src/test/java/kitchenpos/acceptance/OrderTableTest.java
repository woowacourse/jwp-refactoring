package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance;
import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static kitchenpos.fixture.OrderTableFixture.REQUEST;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest extends Acceptance {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 주문_테이블을_생성한다() {
        // given
        CreateOrderTableRequest request = REQUEST.주문_테이블_생성_요청_3명();

        // when
        ExtractableResponse<Response> response = 테이블_생성(request);

        // then
        Assertions.assertThat(response.header("Location"))
                .isNotBlank();
    }

    @Test
    void 주문_테이블을_전체_조회한다() {
        // given
        CreateOrderTableRequest request = REQUEST.주문_테이블_생성_요청_3명();
        ExtractableResponse<Response> createTableResponse = 테이블_생성(request);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .when().get("/api/tables")
                .then().log().all()
                .statusCode(200)
                .extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.body().jsonPath().getList(".", OrderTableResponse.class)).hasSize(1);
            softly.assertThat(response.body().jsonPath().getLong("[0].id")).isEqualTo(createTableResponse.body().jsonPath().getLong("id"));
            softly.assertThat(response.body().jsonPath().getLong("[0].numberOfGuests")).isEqualTo(createTableResponse.body().jsonPath().getLong("numberOfGuests"));
            softly.assertThat(response.body().jsonPath().getBoolean("[0].empty")).isEqualTo(createTableResponse.body().jsonPath().getBoolean("empty"));
        });
    }

    @Test
    void 주문_테이블을_비움_처리한다() {
        // given
        CreateOrderTableRequest request = REQUEST.주문_테이블_생성_요청_3명();
        ExtractableResponse<Response> createTableResponse = 테이블_생성(request);
        Long tableId = Long.parseLong(createTableResponse.header("Location").split("/")[3]);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .body(REQUEST.주문_테이블_비움_요청())
                .contentType(ContentType.JSON)
                .when().put("/api/tables/{orderTableId}/empty", tableId)
                .then().log().all()
                .statusCode(200)
                .extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.body().jsonPath().getLong("id")).isEqualTo(tableId);
            softly.assertThat(response.body().jsonPath().getLong("numberOfGuests")).isEqualTo(request.getNumberOfGuests());
            softly.assertThat(response.body().jsonPath().getBoolean("empty")).isEqualTo(true);
        });
    }

    @Test
    void 주문_테이블의_손님_수를_변경한다() {
        // given
        CreateOrderTableRequest request = REQUEST.주문_테이블_생성_요청_3명();
        ExtractableResponse<Response> createTableResponse = 테이블_생성(request);
        Long tableId = Long.parseLong(createTableResponse.header("Location").split("/")[3]);
        int changeNumberOfGuests = 5;

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .body(REQUEST.주문_테이블_인원_변경_요청(changeNumberOfGuests))
                .contentType(ContentType.JSON)
                .when().put("/api/tables/{orderTableId}/number-of-guests", tableId)
                .then().log().all()
                .statusCode(200)
                .extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.body().jsonPath().getLong("id")).isEqualTo(tableId);
            softly.assertThat(response.body().jsonPath().getLong("numberOfGuests")).isEqualTo(changeNumberOfGuests);
            softly.assertThat(response.body().jsonPath().getBoolean("empty")).isEqualTo(false);
        });
    }

    private static ExtractableResponse<Response> 테이블_생성(CreateOrderTableRequest request) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/tables")
                .then().log().all()
                .statusCode(201)
                .extract();
    }
}
