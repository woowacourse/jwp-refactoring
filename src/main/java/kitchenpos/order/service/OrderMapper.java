package kitchenpos.order.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final MenuRepository menuRepository;

    public OrderMapper(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Order mappingToOrder(final OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = toOrderLineItems(request.getOrderLineItems());
        return request.toEntity(orderLineItems);
    }

    private List<OrderLineItem> toOrderLineItems(final List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request) {
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(MenuNotFoundException::new);
        OrderMenu orderMenu = new OrderMenu(menu.getName(), menu.getPrice());
        return new OrderLineItem(orderMenu, request.getQuantity());
    }
}
