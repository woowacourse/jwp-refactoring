package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.application.dto.OrderChangeDto;
import kitchenpos.order.application.dto.OrderCreateDto;
import kitchenpos.order.application.dto.OrderLineItemCreateDto;

public class OrderFixture {

    public static OrderCreateDto 주문_생성_요청(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        List<OrderLineItemCreateDto> orderLineItemDtos = orderLineItems.stream()
                .map(it -> new OrderLineItemCreateDto(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new OrderCreateDto(orderTableId, orderLineItemDtos);
    }

    public static OrderChangeDto 주문상태_변경_요청(final OrderStatus status) {
        return new OrderChangeDto(status.name());
    }

    public static Order 주문(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTable.getId(), orderLineItems);
    }
}
