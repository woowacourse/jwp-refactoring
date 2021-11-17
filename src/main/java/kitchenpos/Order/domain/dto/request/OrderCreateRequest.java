package kitchenpos.Order.domain.dto.request;

import kitchenpos.Menu.domain.Menu;
import kitchenpos.Order.domain.Order;
import kitchenpos.Order.domain.OrderLineItem;
import kitchenpos.OrderTable.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateRequest {

    private long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderCreateRequest() {
    }

    public Order toEntity() {
        return new Order(
                new OrderTable(orderTableId),
                orderLineItems.stream()
                        .map(item -> new OrderLineItem(
                                new Menu(item.menuId),
                                item.quantity
                        ))
                        .collect(Collectors.toList())
        );
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItemRequest {

        private long menuId;
        private long quantity;

        protected OrderLineItemRequest() {
        }

        public long getMenuId() {
            return menuId;
        }

        public long getQuantity() {
            return quantity;
        }
    }

}
