package kitchenpos.application.response;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseAssembler {

    public List<OrderResponse> orderResponses(final List<Order> orders) {
        return orders.stream()
                .map(this::orderResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderResponse orderResponse(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItemResponses(order.getOrderLineItems())
        );
    }

    private List<OrderLineItemResponse> orderLineItemResponses(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemResponse(
                        orderLineItem.getSeq(),
                        orderLineItem.getOrderId(),
                        orderLineItem.getMenuId(),
                        orderLineItem.getQuantity()
                ))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<MenuResponse> menuResponses(final List<Menu> menus) {
        return menus.stream()
                .map(this::menuResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public MenuResponse menuResponse(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menuProductResponses(menu.getMenuProducts())
        );
    }

    private List<MenuProductResponse> menuProductResponses(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> new MenuProductResponse(
                        menuProduct.getSeq(),
                        menuProduct.getMenuId(),
                        menuProduct.getProductId(),
                        menuProduct.getQuantity()
                ))
                .collect(Collectors.toUnmodifiableList());
    }
}
