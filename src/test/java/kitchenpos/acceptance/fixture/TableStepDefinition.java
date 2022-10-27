package kitchenpos.acceptance.fixture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.http.HttpStatus;

public class TableStepDefinition {

    public static long 테이블을_생성한다(
       final long tableGroupId,
       final int numberOfGuests
    ) {
        OrderTable orderTable = new OrderTable(tableGroupId, numberOfGuests, false);

        return RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().log().all()
            .post("/api/tables")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().jsonPath().getLong("id");
    }

    public static List<OrderTable> 테이블을_조회한다() {
        return RestAssured.given().log().all()
            .when().log().all()
            .get("/api/tables")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", OrderTable.class);
    }

    public static void 테이블의_상태를_변경한다(
        final long orderId,
        final OrderStatus orderStatus
        ) {

//        Order order = new Order();

    }
}
