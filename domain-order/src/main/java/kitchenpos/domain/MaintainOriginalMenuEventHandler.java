package kitchenpos.domain;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MaintainOriginalMenuEventHandler {
    private MenuRepository menuRepository;
    private OrderLineItemRepository orderLineItemRepository;

    public MaintainOriginalMenuEventHandler(
            MenuRepository menuRepository,
            OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(MenuChangedEvent event) {
        Menu savedMenu = menuRepository.save(event.getMenu());
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByMenuId(event.getOriginalMenuId());
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeMenuId(savedMenu.getId());
        }
    }
}
