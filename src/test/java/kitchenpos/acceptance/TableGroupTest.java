package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static kitchenpos.fixture.TableGroupFixture.REQUEST;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupTest extends Acceptance {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 테이블_그룹을_생성한다() {
        // given
        CreateOrderTableRequest request = OrderTableFixture.REQUEST.주문_테이블_생성_요청_빈_테이블();
        Long orderTable = 테이블_생성(request);
        Long orderTable2 = 테이블_생성(request);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .body(REQUEST.주문_테이블_그룹_생성_요청(orderTable, orderTable2))
                .contentType(ContentType.JSON)
                .when().post("/api/table-groups")
                .then().log().all()
                .statusCode(201)
                .extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.header("Location")).isNotBlank();
            softly.assertThat(response.body().jsonPath().getLong("id")).isNotNull();
        });
    }


    private static Long 테이블_생성(CreateOrderTableRequest request) {
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/tables")
                .then().log().all()
                .statusCode(201)
                .extract();

        return Long.parseLong(response.header("Location").split("/")[3]);
    }
}
