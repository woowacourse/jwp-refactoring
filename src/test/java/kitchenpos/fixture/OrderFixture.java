package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderChangeDto;
import kitchenpos.dto.OrderCreateDto;
import kitchenpos.dto.OrderLineItemCreateDto;

public class OrderFixture {

    public static OrderCreateDto 주문_생성_요청(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        List<OrderLineItemCreateDto> orderLineItemDtos = orderLineItems.stream()
                .map(it -> new OrderLineItemCreateDto(it.getMenu().getId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new OrderCreateDto(orderTableId, orderLineItemDtos);
    }

    public static OrderChangeDto 주문상태_변경_요청(final OrderStatus status) {
        return new OrderChangeDto(status.name());
    }

    public static Order 주문(OrderTable orderTable) {
        return new Order(orderTable);
    }

    public static Order 주문(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTable);
        order.addOrderLineItems(orderLineItems);
        return order;
    }
}
