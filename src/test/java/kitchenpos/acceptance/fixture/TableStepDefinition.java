package kitchenpos.acceptance.fixture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
import org.springframework.http.HttpStatus;

public class TableStepDefinition {

    public static long 테이블을_생성한다(
       final int numberOfGuests,
       final boolean empty
    ) {
        OrderTableCreateRequest orderTable = new OrderTableCreateRequest(numberOfGuests, empty);

        return RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().log().all()
            .post("/api/tables")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().jsonPath().getLong("id");
    }

    public static List<OrderTableResponse> 테이블을_조회한다() {
        return RestAssured.given().log().all()
            .when().log().all()
            .get("/api/tables")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", OrderTableResponse.class);
    }

    public static void 테이블의_상태를_변경한다(
        final long orderTableId
        ) {

        OrderTableChangeEmptyRequest orderTable = new OrderTableChangeEmptyRequest(true);

        RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().log().all()
            .put("/api/tables/" + orderTableId + "/empty")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    public static void 테이블의_손님의_숫자를_변경한다(
        final long orderTableId,
        final int numberOfGuests) {

        OrderTableChangeNumberOfGuestsRequest orderTable = new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);

        RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().log().all()
            .put("/api/tables/" + orderTableId + "/number-of-guests")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }
}
