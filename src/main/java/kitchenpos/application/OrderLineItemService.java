package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.collection.OrderLineItems;
import kitchenpos.domain.entity.OrderLineItem;
import kitchenpos.repository.OrderLineItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderLineItemService {

    private OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemService(OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public void saveOrderLineItems(OrderLineItems orderLineItems) {
        orderLineItemRepository.saveAll(orderLineItems.getElements());
    }

    public OrderLineItem findOrderLineItem(Long orderLineItemId) {
        return orderLineItemRepository.findById(orderLineItemId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public OrderLineItems findOrderLineItems(List<Long> orderLineItemIds) {
        List<OrderLineItem> orderLineItems = orderLineItemIds.stream()
                .map(this::findOrderLineItem)
                .collect(Collectors.toList());
        return new OrderLineItems(orderLineItems);
    }

    public OrderLineItems findOrderLineItemsInOrder(Long orderId) {
        return new OrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));
    }
}
