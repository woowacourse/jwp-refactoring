package kitchenpos.orderlineitem.application;

import kitchenpos.order.SaveOrderLineItemsEvent;
import kitchenpos.order.application.request.OrderLineItemDto;
import kitchenpos.orderlineitem.OrderLineItem;
import kitchenpos.orderlineitem.OrderLineQuantity;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderLineItemService {
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemService(final OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @EventListener
    public void saveOrderLineItems(final SaveOrderLineItemsEvent event) {
        for (final OrderLineItemDto orderLineItemDto : event.getOrderLineItemsDto()) {
            final OrderLineItem orderLineItem = new OrderLineItem(
                    event.getOrder().getId(),
                    orderLineItemDto.getMenuId(),
                    new OrderLineQuantity(orderLineItemDto.getQuantity())
            );
            orderLineItemRepository.save(orderLineItem);
        }
    }
}
