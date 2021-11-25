package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuUpdatedEvent;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MenuUpdatedHandler {

    private final MenuRepository menuRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public MenuUpdatedHandler(
        MenuRepository menuRepository,
        OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Async
    @Transactional
    @EventListener
    public void handle(MenuUpdatedEvent event) {
        Menu updatedMenu = menuRepository.save(event.getUpdatedMenu());

        for (OrderLineItem orderLineItem : orderLineItemRepository.findAllByMenuId(event.getMenuId())) {
            orderLineItem.updateMenuId(updatedMenu.getId());
        }
    }
}
