package kitchenpos.common.event.listener;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.exception.MenuNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MenuEventListener {

    private final MenuService menuService;

    public MenuEventListener(final MenuService menuService) {
        this.menuService = menuService;
    }

    public void validateMenuExist(final Long menuId) {
        if (!menuService.isExistMenuById(menuId)) {
            throw new MenuNotFoundException();
        }
    }
}
