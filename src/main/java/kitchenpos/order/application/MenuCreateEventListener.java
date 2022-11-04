package kitchenpos.order.application;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.MenuCreateEvent;
import kitchenpos.order.dao.OrderMenuDao;
import kitchenpos.order.domain.OrderMenu;

@Component
public class MenuCreateEventListener {

    private final OrderMenuDao orderMenuDao;

    public MenuCreateEventListener(final OrderMenuDao orderMenuDao) {
        this.orderMenuDao = orderMenuDao;
    }

    @EventListener
    public void handleEvent(final MenuCreateEvent event) {
        orderMenuDao.save(new OrderMenu(event.getMenuId(), event.getName(), event.getPrice()));
    }
}
