package kitchenpos.order.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.service.OrderRequest.OrderLineItemRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    private final MenuRepository menuRepository;

    public OrderMapper(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Order mapFrom(OrderRequest orderRequest) {
        return new Order(
                orderRequest.getOrderTableId(),
                orderLineItemList(orderRequest.getOrderLineItems())
        );
    }

    private List<OrderLineItem> orderLineItemList(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream().map(this::toOrderLineItem).collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow(IllegalArgumentException::new);
        return new OrderLineItem(
                orderLineItemRequest.getMenuId(),
                menu.getName(),
                menu.getPrice(),
                orderLineItemRequest.getQuantity());
    }
}
