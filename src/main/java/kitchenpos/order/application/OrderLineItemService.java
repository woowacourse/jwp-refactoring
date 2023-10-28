package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

    public List<OrderLineItem> create(Order order, List<OrderLineItemDto> orderLineItemDtos) {
        List<OrderLineItem> orderLineItems = toEntity(order, orderLineItemDtos);
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 포함되어 있지 않습니다.");
        }
        return saveAll(orderLineItems);
    }

    private List<OrderLineItem> toEntity(Order order, List<OrderLineItemDto> orderLineItems) {
        return orderLineItems.stream()
                             .map(orderLineItem -> new OrderLineItem(
                                     order,
                                     orderLineItem.getMenuId(),
                                     orderLineItem.getQuantity()
                             ))
                             .collect(Collectors.toList());
    }

    private List<OrderLineItem> saveAll(List<OrderLineItem> orderLineItems) {
        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItem orderLineItem : orderLineItems) {
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        return savedOrderLineItems;
    }
}
