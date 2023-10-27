package kitchenpos.order.eventlistener;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuCreatedEvent;
import kitchenpos.order.dao.OrderedMenuDao;
import kitchenpos.order.domain.OrderedMenu;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MenuCreatedEventListener {

    private final OrderedMenuDao orderedMenuDao;
    private final MenuDao menuDao;

    public MenuCreatedEventListener(OrderedMenuDao orderedMenuDao, MenuDao menuDao) {
        this.orderedMenuDao = orderedMenuDao;
        this.menuDao = menuDao;
    }

    @EventListener
    public void create(MenuCreatedEvent event) {
        Menu menu = menuDao.findById(event.getId())
                .orElseThrow(IllegalArgumentException::new);
        OrderedMenu orderedMenu = OrderedMenu.from(menu);
        orderedMenuDao.save(orderedMenu);
    }
}
