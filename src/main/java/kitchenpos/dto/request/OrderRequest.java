package kitchenpos.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderMenu;
import kitchenpos.domain.OrderMenuProduct;
import kitchenpos.domain.OrderStatus;
import kitchenpos.exceptions.EntityNotExistException;

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
                .map(it -> new OrderLineItem(null, null, createOrderMenu(it.getMenuId(), menus), it.getQuantity()))
                .collect(Collectors.toList());
        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    private OrderMenu createOrderMenu(final Long menuId, final List<Menu> menus) {
        final Menu menu = menus.stream()
                .filter(it -> it.getId().equals(menuId))
                .findFirst()
                .orElseThrow(EntityNotExistException::new);
        return new OrderMenu(menu.getName(), menu.getPrice().getValue(), menu.getMenuGroup().getName(),
                convertToOrderMenuProducts(menu));
    }

    private List<OrderMenuProduct> convertToOrderMenuProducts(final Menu menu) {
        return menu.getMenuProducts()
                .stream()
                .map(it -> new OrderMenuProduct(null, null, it.getProduct().getName(),
                        it.getProduct().getPrice().getValue(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
