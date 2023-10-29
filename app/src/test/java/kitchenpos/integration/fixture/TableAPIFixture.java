package kitchenpos.integration.fixture;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import org.springframework.http.MediaType;
import java.util.List;

public class TableAPIFixture {

    public static final int DEFAULT_ORDER_TABLE_NUMBER_OF_GUESTS = 5;
    public static final boolean DEFAULT_ORDER_TABLE_EMPTY = false;

    public static ExtractableResponse<Response> createOrderTable(final OrderTableCreateRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(request)
                .post("/api/tables")
                .then()
                .log().all()
                .extract();
    }

    public static OrderTableResponse createOrderTableAndReturnResponse(final OrderTableCreateRequest request) {
        return createOrderTable(request).as(OrderTableResponse.class);
    }

    public static OrderTableResponse createDefaultOrderTable() {
        final OrderTableCreateRequest request = new OrderTableCreateRequest(DEFAULT_ORDER_TABLE_NUMBER_OF_GUESTS, DEFAULT_ORDER_TABLE_EMPTY);
        return createOrderTableAndReturnResponse(request);
    }

    public static List<OrderTableResponse> listOrderTables() {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/tables")
                .then()
                .log().all()
                .extract();
        return response.as(new TypeRef<>() {
        });
    }

    public static OrderTableResponse changeOrderEmpty(final Long orderTableId,
                                                      final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(orderTableChangeEmptyRequest)
                .put("/api/tables/{orderTableId}/empty", orderTableId)
                .then()
                .log().all()
                .extract();
        return response.as(OrderTableResponse.class);
    }

    public static OrderTableResponse changeOrderNumberOfGuests(final Long orderTableId,
                                                               final OrderTableChangeNumberOfGuestRequest orderTableChangeNumberOfGuestRequest) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(orderTableChangeNumberOfGuestRequest)
                .put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .then()
                .log().all()
                .extract();
        return response.as(OrderTableResponse.class);
    }
}
