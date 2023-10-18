package kitchenpos.acceptance.oreder;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static kitchenpos.acceptance.AcceptanceSteps.생성된_ID를_추출한다;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderCreateRequest.OrderLineItemInfo;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

@SuppressWarnings("NonAsciiCharacters")
public class OrderAcceptanceSteps {

    public static OrderLineItemInfo 주문_항목_요청(
            Long 메뉴_ID,
            int 수량
    ) {
        return new OrderLineItemInfo(메뉴_ID, 수량);
    }

    public static OrderLineItem 주문_항목(
            Long 주문_ID,
            Long 메뉴_ID,
            int 수량
    ) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(주문_ID);
        orderLineItem.setMenuId(메뉴_ID);
        orderLineItem.setQuantity(수량);
        return orderLineItem;
    }

    public static Long 주문_생성후_ID를_가져온다(
            Long 테이블_ID, OrderLineItemInfo... 주문_항목들
    ) {
        return 생성된_ID를_추출한다(주문_생성_요청을_보낸다(테이블_ID, 주문_항목들));
    }

    public static ExtractableResponse<Response> 주문_생성_요청을_보낸다(
            Long 테이블_ID, OrderLineItemInfo... 주문_항목들
    ) {
        OrderCreateRequest request = new OrderCreateRequest(테이블_ID, Arrays.asList(주문_항목들));
        return given()
                .body(request)
                .post("/api/orders")
                .then()
                .log().all()
                .extract();
    }

    public static OrderStatusChangeRequest 주문_상태_변경_요청(OrderStatus orderStatus) {
        return new OrderStatusChangeRequest(orderStatus.name());
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청을_보낸다(Long 주문_ID, OrderStatus 주문_상태) {
        return given()
                .body(주문_상태_변경_요청(주문_상태))
                .put("/api/orders/{orderId}/order-status", 주문_ID)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_조회_요청을_보낸다() {
        return given()
                .get("/api/orders")
                .then().log().headers()
                .extract();
    }

    public static Order 주문(Long 주문_ID, Long 테이블_ID, OrderStatus 주문_상태, OrderLineItem... 주문_항목들) {
        Order order = new Order();
        order.setId(주문_ID);
        order.setOrderTableId(테이블_ID);
        order.setOrderStatus(주문_상태.name());
        order.setOrderLineItems(Arrays.asList(주문_항목들));
        return order;
    }

    public static void 주문_조회_결과를_검증한다(
            ExtractableResponse<Response> 응답,
            Order... 예상_주문들
    ) {
        List<Order> orders = 응답.as(new TypeRef<>() {
        });
        assertThat(orders)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(Arrays.asList(예상_주문들));
    }
}
