package kitchenpos.eventlistener;

import kitchenpos.event.ValidateMenuExistsEvent;
import kitchenpos.service.MenuService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MenuEventListener {

    private final MenuService menuService;

    public MenuEventListener(MenuService menuService) {
        this.menuService = menuService;
    }

    @EventListener
    public void validateMenuExists(ValidateMenuExistsEvent event) {
        menuService.validateMenuExists(event.getMenuId());
    }
}
