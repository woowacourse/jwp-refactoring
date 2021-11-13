package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.stereotype.Service;

@Service
public class OrderLineItemService {

    private final OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemService(OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    public void saveAll(List<OrderLineItem> orderLineItems) {
        orderLineItemRepository.saveAll(orderLineItems);
    }
}
