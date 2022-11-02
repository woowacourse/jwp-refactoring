package kitchenpos.application;

import kitchenpos.domain.collection.OrderLineItems;
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
}
