package kitchenpos.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.application.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.application.dto.request.UpdateOrderTableGuestsRequest;
import kitchenpos.application.dto.response.CreateOrderTableResponse;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.table.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static class REQUEST {
        public static CreateOrderTableRequest 주문_테이블_생성_요청_3명() {
            return CreateOrderTableRequest.builder()
                    .numberOfGuests(3)
                    .empty(false)
                    .build();
        }

        public static CreateOrderTableRequest 주문_테이블_생성_요청_빈_테이블() {
            return CreateOrderTableRequest.builder()
                    .numberOfGuests(0)
                    .empty(true)
                    .build();
        }

        public static UpdateOrderTableEmptyRequest 주문_테이블_비움_요청() {
            return new UpdateOrderTableEmptyRequest(true);
        }

        public static UpdateOrderTableGuestsRequest 주문_테이블_인원_변경_요청(int numberOfGuests) {
            return new UpdateOrderTableGuestsRequest(numberOfGuests);
        }
    }

    public static class RESPONSE {
        public static CreateOrderTableResponse 주문_테이블_생성_3명_응답() {
            return CreateOrderTableResponse.builder()
                    .id(1L)
                    .tableGroupId(1L)
                    .numberOfGuests(3)
                    .empty(false)
                    .build();
        }

        public static OrderTableResponse 주문_테이블_3명_응답() {
            return OrderTableResponse.builder()
                    .id(1L)
                    .numberOfGuests(3)
                    .empty(false)
                    .build();
        }

        public static OrderTableResponse 주문_테이블_N명_응답(int numberOfGuests) {
            return OrderTableResponse.builder()
                    .id(1L)
                    .numberOfGuests(numberOfGuests)
                    .empty(false)
                    .build();
        }
    }

    public static class ORDER_TABLE {
        public static OrderTable 주문_테이블_1() {
            return OrderTable.builder()
                    .id(1L)
                    .numberOfGuests(3)
                    .empty(false)
                    .build();
        }

        public static OrderTable 주문_테이블_1_비어있는가(boolean empty) {
            return OrderTable.builder()
                    .id(1L)
                    .numberOfGuests(empty ? 0 : 3)
                    .empty(empty)
                    .build();
        }

        public static OrderTable 비어있는_테이블() {
            return OrderTable.builder()
                    .id(1L)
                    .empty(true)
                    .build();
        }

        public static OrderTable 주문_테이블_2() {
            return OrderTable.builder()
                    .id(2L)
                    .numberOfGuests(3)
                    .empty(true)
                    .build();
        }
    }

    public static Long 주문_테이블_생성(CreateOrderTableRequest request) {
        ExtractableResponse<Response> response = RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .when().post("/api/tables")
                .then().log().all()
                .statusCode(201)
                .extract();
        return Long.parseLong(response.header("Location").split("/")[3]);
    }
}
