package kitchenpos.menugroup.eventlistener;

import kitchenpos.menu.dto.MenuCreatedEvent;
import kitchenpos.menugroup.dao.MenuGroupDao;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MenuEventListener {

    private final MenuGroupDao menuGroupDao;

    public MenuEventListener(MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @EventListener
    public void createMenu(MenuCreatedEvent event) {
        validateMenuGroup(event);
    }

    private void validateMenuGroup(MenuCreatedEvent event) {
        if (!menuGroupDao.existsById(event.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }
}
