package kitchenpos.common.event.listener;

import kitchenpos.common.event.message.ValidatorMenuExist;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.exception.MenuNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MenuEventListener {

    private final MenuService menuService;

    public MenuEventListener(final MenuService menuService) {
        this.menuService = menuService;
    }

    @EventListener
    private void validateMenuExist(final ValidatorMenuExist validatorMenuExist) {
        if (!menuService.isExistMenuById(validatorMenuExist.getMenuId())) {
            throw new MenuNotFoundException();
        }
    }
}
