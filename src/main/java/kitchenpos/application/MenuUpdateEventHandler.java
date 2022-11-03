package kitchenpos.application;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuUpdateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MenuUpdateEventHandler {

    private final MenuService menuService;
    private final OrderLineItemDao orderLineItemDao;

    public MenuUpdateEventHandler(final MenuService menuService, final OrderLineItemDao orderLineItemDao) {
        this.menuService = menuService;
        this.orderLineItemDao = orderLineItemDao;
    }

    @EventListener
    @Transactional
    public void handle(final MenuUpdateEvent menuUpdateEvent) {
        if (!orderLineItemDao.existsByMenuId(menuUpdateEvent.getOriginalMenuId())) {
            return;
        }
        final Long menuId = menuService.create(menuUpdateEvent.getMenu()).getId();
        orderLineItemDao.updateMenuIds(menuUpdateEvent.getOriginalMenuId(), menuId);
    }
}
