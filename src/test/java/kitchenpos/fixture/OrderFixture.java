package kitchenpos.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.request.CreateOrderRequest;

import java.util.List;

import static kitchenpos.dto.request.CreateOrderRequest.CreateOrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    private OrderFixture() {
    }

    public static class REQUEST {

        public static CreateOrderRequest 주문_생성_요청() {
            Long orderTableId = OrderTableFixture.주문_테이블_생성(OrderTableFixture.REQUEST.주문_테이블_생성_요청_3명());
            Long menuId = MenuFixture.메뉴_생성(MenuFixture.REQUEST.후라이드_치킨_16000원_1마리_등록_요청());
            return CreateOrderRequest.builder()
                    .orderTableId(orderTableId)
                    .orderLineItems(List.of(new CreateOrderLineItem(menuId, 1L)))
                    .build();
        }
    }

    public static Long 주문_생성(CreateOrderRequest request) {
        ExtractableResponse<Response> response = RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(201)
                .extract();
        return Long.parseLong(response.header("Location").split("/")[3]);
    }
}
