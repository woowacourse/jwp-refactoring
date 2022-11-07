package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.exception.NotFoundMenuException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toOrder(final OrderCreateRequest orderCreateRequest, final List<Menu> menus) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderCreateRequest.getOrderLineItems()) {
            final OrderMenu orderMenu = getOrderMenu(menus, orderLineItemRequest);
            final OrderLineItem orderLineItem = orderLineItemRequest.toOrderLineItem(orderMenu);
            orderLineItems.add(orderLineItem);
        }
        return new Order(null, orderCreateRequest.getOrderTableId(), null, null, orderLineItems);
    }

    private OrderMenu getOrderMenu(final List<Menu> menus, final OrderLineItemRequest orderLineItemRequest) {
        final Long menuId = orderLineItemRequest.getMenuId();
        final Menu menu = menus.stream()
                .filter(it -> it.getId().equals(menuId))
                .findFirst()
                .orElseThrow(NotFoundMenuException::new);
        return new OrderMenu(null, menu.getName(), menu.getPrice());
    }
}
