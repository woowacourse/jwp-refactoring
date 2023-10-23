package kitchenpos.fixture;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderUpdateStatusRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문_망고치킨_2개(final OrderStatus orderStatus) {
        return new Order(OrderTableFixture.주문테이블_N명(2), COOKING, LocalDateTime.now(),
                Collections.singletonList(OrderLineItemFixture.주문항목_망고치킨_2개()));
    }

    public static Order 주문_망고치킨_2개_빈주문항목(final OrderStatus orderStatus) {
        return new Order(OrderTableFixture.주문테이블_N명(2), COOKING, LocalDateTime.now());
    }

    public static OrderCreateRequest 주문요청_생성(final OrderTable orderTable, final OrderLineItem... orderLineItems) {
        final var orderLineItemRequests = Arrays.stream(orderLineItems)
                .map(it -> new OrderLineItemRequest(it.getMenu().getId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new OrderCreateRequest(orderTable.getId(), orderLineItemRequests);
    }

    public static OrderUpdateStatusRequest 주문요청_상태변경_생성(final OrderStatus orderStatus) {
        return new OrderUpdateStatusRequest(orderStatus.name());
    }
}
