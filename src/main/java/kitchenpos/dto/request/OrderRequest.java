package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderMenuRequest> orderMenus;

    public OrderRequest() {
    }

    public OrderRequest(final Long orderTableId, final List<OrderMenuRequest> orderMenus) {
        this.orderTableId = orderTableId;
        this.orderMenus = orderMenus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderMenuRequest> getOrderMenus() {
        return orderMenus;
    }

    public Order toEntity(final List<Menu> menus) {
        final List<OrderLineItem> orderLineItems = orderMenus.stream()
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
