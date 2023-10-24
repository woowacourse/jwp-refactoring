package kitchenpos.fixture;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderUpdateStatusRequest;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

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
