package kitchenpos.fixture;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.application.dto.response.CreateOrderResponse;
import kitchenpos.application.dto.response.OrderLineItemResponse;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.MENU;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    private OrderFixture() {
    }

    public static class ORDER {

        public static Order 주문_요청_조리중() {
            return Order.builder()
                    .id(1L)
                    .orderTableId(ORDER_TABLE.주문_테이블_1().getId())
                    .orderStatus(OrderStatus.COOKING)
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(getOrderLineItems(1L, 1L, 1L))
                    .build();
        }

        public static Order 주문_요청_식사중() {
            return Order.builder()
                    .id(2L)
                    .orderTableId(ORDER_TABLE.주문_테이블_1().getId())
                    .orderStatus(OrderStatus.MEAL)
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(getOrderLineItems(2L, 2L, 2L))
                    .build();
        }

        public static Order 주문_요청_계산_완료() {
            return Order.builder()
                    .id(3L)
                    .orderTableId(ORDER_TABLE.주문_테이블_1().getId())
                    .orderStatus(OrderStatus.COMPLETION)
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(getOrderLineItems(3L, 3L, 3L))
                    .build();
        }

        public static Order 주문_요청_현재상태는(OrderStatus orderStatus) {
            return Order.builder()
                    .id(3L)
                    .orderTableId(ORDER_TABLE.주문_테이블_1().getId())
                    .orderStatus(orderStatus)
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(getOrderLineItems(3L, 3L, 3L))
                    .build();
        }

        private static List<OrderLineItem> getOrderLineItems(Long... orderId) {
            List<OrderLineItem> orderLineItems = new ArrayList<>();
            for (Long id : orderId) {
                OrderLineItem orderLineItem = OrderLineItem.builder()
                        .seq(1L)
                        .menu(MENU.후라이드_치킨_16000원_1마리())
                        .quantity(1L)
                        .build();
                orderLineItems.add(orderLineItem);
            }
            return orderLineItems;
        }
    }

    public static class RESPONSE {

        public static CreateOrderResponse 주문_생성_응답_조리중() {
            OrderLineItemResponse orderLineItemResponse = OrderLineItemResponse.builder()
                    .seq(1L)
                    .menuId(1L)
                    .quantity(1L)
                    .build();
            return CreateOrderResponse.builder()
                    .id(1L)
                    .orderTableId(1L)
                    .orderStatus(OrderStatus.COOKING.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(List.of(orderLineItemResponse))
                    .build();
        }

        public static OrderResponse 주문_식사중_응답() {
            OrderLineItemResponse orderLineItemResponse = OrderLineItemResponse.builder()
                    .seq(1L)
                    .menuId(1L)
                    .quantity(1L)
                    .build();
            return OrderResponse.builder()
                    .id(1L)
                    .orderTableId(1L)
                    .orderStatus(OrderStatus.MEAL.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(List.of(orderLineItemResponse))
                    .build();
        }

        public static OrderResponse 주문_조리중_응답() {
            OrderLineItemResponse orderLineItemResponse = OrderLineItemResponse.builder()
                    .seq(1L)
                    .menuId(1L)
                    .quantity(1L)
                    .build();
            return OrderResponse.builder()
                    .id(1L)
                    .orderTableId(1L)
                    .orderStatus(OrderStatus.COOKING.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(List.of(orderLineItemResponse))
                    .build();
        }

        public static OrderResponse 주문_계산완료_응답() {
            OrderLineItemResponse orderLineItemResponse = OrderLineItemResponse.builder()
                    .seq(1L)
                    .menuId(1L)
                    .quantity(1L)
                    .build();
            return OrderResponse.builder()
                    .id(1L)
                    .orderTableId(1L)
                    .orderStatus(OrderStatus.COMPLETION.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(List.of(orderLineItemResponse))
                    .build();
        }
    }

    public static class REQUEST {

        public static CreateOrderRequest 주문_생성_요청() {
            return CreateOrderRequest.builder()
                    .orderTableId(1L)
                    .orderStatus(OrderStatus.COOKING.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItemIds(List.of(1L, 2L))
                    .build();
        }

        public static CreateOrderRequest 주문_생성_요청_주문항목(List<Long> orderLineItemIds) {
            return CreateOrderRequest.builder()
                    .orderTableId(1L)
                    .orderStatus(OrderStatus.COOKING.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItemIds(orderLineItemIds)
                    .build();
        }


        public static CreateOrderRequest 주문_생성_요청_비어있는_주문항목() {
            return CreateOrderRequest.builder()
                    .orderTableId(1L)
                    .orderStatus(OrderStatus.COOKING.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItemIds(Collections.emptyList())
                    .build();
        }

        public static UpdateOrderStatusRequest 주문_상태_변경_요청(String orderStatus) {
            return new UpdateOrderStatusRequest(orderStatus);
        }
    }

    public static class ORDER_LINE_ITEM {

        public static OrderLineItem 주문_항목_아이템_1개(Long orderId) {
            return OrderLineItem.builder()
                    .seq(1L)
                    .menu(MENU.후라이드_치킨_16000원_1마리())
                    .quantity(1L)
                    .build();
        }
    }
}
