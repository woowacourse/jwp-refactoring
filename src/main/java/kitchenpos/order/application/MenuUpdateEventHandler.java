package kitchenpos.order.application;

import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuUpdateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MenuUpdateEventHandler {

    private final MenuService menuService;
    private final OrderLineItemRepository orderLineItemRepository;

    public MenuUpdateEventHandler(MenuService menuService,
                                  OrderLineItemRepository orderLineItemRepository) {
        this.menuService = menuService;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @EventListener
    @Transactional
    public void handle(MenuUpdateEvent event) {
        if(!orderLineItemRepository.existsByMenuId(event.getOriginalId())) {
            return;
        }
        final Long newId = menuService.create(event.getMenu()).getId();
        orderLineItemRepository.updateMenuIds(event.getOriginalId(), newId);
    }
}
