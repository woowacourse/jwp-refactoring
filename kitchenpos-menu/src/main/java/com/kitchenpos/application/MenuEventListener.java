package com.kitchenpos.application;

import com.kitchenpos.event.message.ValidatorMenuExist;
import com.kitchenpos.exception.MenuNotFoundException;
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
