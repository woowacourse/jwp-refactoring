package kitchenpos.order.application;

import kitchenpos.menu.domain.vo.Quantity;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public Order map(final OrderSheet orderSheet) {
        return new Order(
                orderSheet.getOrderTableId(),
                getOrderLineItems(orderSheet)
        );
    }

    private OrderLineItems getOrderLineItems(final OrderSheet orderSheet) {
        return new OrderLineItems(orderSheet.getOrderSheetItems()
                .stream()
                .map(orderSheetItem -> new OrderLineItem(
                        orderSheetItem.getMenuId(),
                        new Quantity(orderSheetItem.getQuantity())
                )).collect(Collectors.toList()));
    }
}
