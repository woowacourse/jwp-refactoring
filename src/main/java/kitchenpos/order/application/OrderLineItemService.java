package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.domain.Name;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.request.OrderLineItemDto;
import org.springframework.stereotype.Service;

@Service
public class OrderLineItemService {

    private final OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemService(OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    public List<OrderLineItem> create(
            Order order,
            List<Menu> menus,
            List<OrderLineItemDto> orderLineItemDtos
    ) {
        validate(menus, orderLineItemDtos);
        List<OrderLineItem> orderLineItems = toEntity(order, menus, orderLineItemDtos);
        return saveAll(orderLineItems);
    }

    private void validate(
            List<Menu> menus,
            List<OrderLineItemDto> orderLineItems
    ) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 포함되어 있지 않습니다.");
        }
        if (menus.size() != orderLineItems.size()) {
            throw new IllegalArgumentException("메뉴가 존재하지 않습니다.");
        }
    }

    private List<OrderLineItem> toEntity(
            Order order,
            List<Menu> menus,
            List<OrderLineItemDto> orderLineItemDtos
    ) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Menu menu : menus) {
            orderLineItems.add(mapOrderLineItem(order, orderLineItemDtos, menu));
        }
        return orderLineItems;
    }

    private OrderLineItem mapOrderLineItem(Order order, List<OrderLineItemDto> orderLineItemDtos, Menu menu) {
        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            if (menu.getId().equals(orderLineItemDto.getMenuId())) {
                return new OrderLineItem(
                        order,
                        menu.getId(),
                        new Name(menu.getName()),
                        menu.getPrice(),
                        orderLineItemDto.getQuantity()
                );
            }
        }
        throw new IllegalArgumentException();
    }

    private List<OrderLineItem> saveAll(List<OrderLineItem> orderLineItems) {
        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItem orderLineItem : orderLineItems) {
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        return savedOrderLineItems;
    }
}
