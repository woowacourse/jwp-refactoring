package kitchenpos.event;

import kitchenpos.domain.menu.MenuChangeEvent;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.repository.OrderLineItemRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MenuChangeEventHandler {

    private final OrderLineItemRepository orderLineItemRepository;

    public MenuChangeEventHandler(OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @EventListener
    @Transactional
    public void handle(MenuChangeEvent event) {
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByMenuId(event.getMenuId());
        for (OrderLineItem orderLineItem : orderLineItems) {
            if (isBefore(orderLineItem.getCreatedAt(), event.getUpdatedAt())) {
                orderLineItem.updateTemporaryMenu(event.getTemporaryMenu());
            }
        }
    }

    private boolean isBefore(LocalDateTime createdDate, LocalDateTime updatedDate) {
        return createdDate.isBefore(updatedDate);
    }
}
