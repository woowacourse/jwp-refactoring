package kitchenpos.event;

import kitchenpos.menu.domain.MenuChangeEvent;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
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
        // TODO 수정을 두번이상 할 경우에 대해 대비하기
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByMenuId(event.getMenuId());
        for (OrderLineItem orderLineItem : orderLineItems) {
            if (isBefore(orderLineItem.getCreatedAt(), event.getUpdatedAt())) {
                orderLineItem.setTemporaryMenu(event.getTemporaryMenu());
            }
        }
    }

    private boolean isBefore(LocalDateTime createdDate, LocalDateTime updatedDate) {
        return createdDate.isBefore(updatedDate);
    }
}
