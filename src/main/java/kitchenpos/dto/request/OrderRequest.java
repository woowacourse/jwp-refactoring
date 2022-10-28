package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderMenuRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(final Long orderTableId, final List<OrderMenuRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderMenuRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toEntity(final List<Menu> menus) {
        final List<OrderLineItem> orderLineItems = this.orderLineItems.stream()
                .map(it -> new OrderLineItem(null, findMenuById(it.getMenuId(), menus), it.getQuantity()))
                .collect(Collectors.toList());
        return new Order(orderTableId, orderLineItems);
    }

    private Menu findMenuById(final Long menuId, final List<Menu> menus) {
        return menus.stream()
                .filter(it -> it.getId().equals(menuId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
