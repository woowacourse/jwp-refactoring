package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.TableCreateRequest;
import kitchenpos.ui.request.TableUpdateEmptyRequest;
import kitchenpos.ui.request.TableUpdateNumberOfGuestsRequest;

import static io.restassured.http.ContentType.JSON;

public class TableStep {

    public static Long 테이블_생성_요청하고_아이디_반환(final OrderTable orderTable) {
        final ExtractableResponse<Response> response = 테이블_생성_요청(orderTable);
        return response.jsonPath().getLong("id");
    }

    public static OrderTable 테이블_생성_요청하고_테이블_반환(final OrderTable orderTable) {
        final ExtractableResponse<Response> response = 테이블_생성_요청(orderTable);
        return response.jsonPath().getObject("", OrderTable.class);
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(final OrderTable orderTable) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(orderTable)

                .when()
                .post("/api/tables")

                .then()
                .log().all()
                .extract();
    }

    public static OrderTable 테이블_생성_요청하고_테이블_반환(final TableCreateRequest request) {
        final ExtractableResponse<Response> response = 테이블_생성_요청(request);
        return response.jsonPath().getObject("", OrderTable.class);
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(final TableCreateRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)

                .when()
                .post("/api/tables")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_조회_요청() {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)

                .when()
                .get("/api/tables")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_상태_Empty로_변경_요청(final OrderTable orderTable) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(orderTable)

                .when()
                .put("/api/tables/" + orderTable.getId() + "/empty")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_상태_Empty로_변경_요청(final Long orderTableId, final TableUpdateEmptyRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)

                .when()
                .put("/api/tables/" + orderTableId + "/empty")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블에_앉은_사람_수_변경_요청(final OrderTable orderTable) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(orderTable)

                .when()
                .put("/api/tables/" + orderTable.getId() + "/number-of-guests")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블에_앉은_사람_수_변경_요청(final Long orderTableId, final TableUpdateNumberOfGuestsRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)

                .when()
                .put("/api/tables/" + orderTableId + "/number-of-guests")

                .then()
                .log().all()
                .extract();
    }
}
