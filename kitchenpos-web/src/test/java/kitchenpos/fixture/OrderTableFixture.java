package kitchenpos.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.dto.request.UpdateOrderTableGuestsRequest;

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
