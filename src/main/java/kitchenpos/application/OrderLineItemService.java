package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class OrderLineItemService {

    private final OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemService(OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    public List<OrderLineItem> findAllByIds(List<Long> orderLineItemIds) {
        final List<OrderLineItem> orderLineItems = orderLineItemIds.stream()
            .map(id -> orderLineItemRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new))
            .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        return orderLineItems;
    }

    public void saveAll(List<OrderLineItem> orderLineItems) {
        orderLineItemRepository.saveAll(orderLineItems);
    }
}
