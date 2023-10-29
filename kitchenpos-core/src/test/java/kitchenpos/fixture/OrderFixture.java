package kitchenpos.fixture;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderUpdateStatusRequest;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static OrderCreateRequest 주문요청_생성(final OrderTable orderTable, final OrderLineItem... orderLineItems) {
        final var orderLineItemRequests = Arrays.stream(orderLineItems)
                .map(it -> new OrderLineItemRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new OrderCreateRequest(orderTable.getId(), orderLineItemRequests);
    }

    public static OrderUpdateStatusRequest 주문요청_상태변경_생성(final OrderStatus orderStatus) {
        return new OrderUpdateStatusRequest(orderStatus.name());
    }
}
