package kitchenpos.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.service.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.service.OrderLineItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderMapper {

    private final MenuService menuService;

    public OrderMapper(MenuService menuService) {
        this.menuService = menuService;
    }

    @Transactional(readOnly = true)
    public Order toEntity(OrderDto orderDto) {
        List<OrderLineItem> orderLineItems = orderDto.getOrderLineItemDtos()
                                                     .stream()
                                                     .map(this::toOrderLineItem)
                                                     .collect(toList());

        return new Order.Builder()
            .id(orderDto.getId())
            .orderStatus(OrderStatus.valueOf(orderDto.getOrderStatus()))
            .orderTableId(orderDto.getOrderTableId())
            .orderedTime(LocalDateTime.now())
            .orderLineItems(orderLineItems)
            .build();
    }

    private OrderLineItem toOrderLineItem(OrderLineItemDto orderLineItemDto) {
        Menu menu = menuService.getById(orderLineItemDto.getMenuId());

        return new OrderLineItem.Builder()
            .orderMenu(new OrderMenu(menu.getId(), menu.getName(), menu.getPrice()))
            .quantity(orderLineItemDto.getQuantity())
            .build();
    }
}
