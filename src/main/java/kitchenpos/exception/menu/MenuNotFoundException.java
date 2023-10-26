package kitchenpos.exception.menu;

import kitchenpos.exception.common.NotFoundException;

public class MenuNotFoundException extends NotFoundException {
    private static final String RESOURCE = "메뉴";

    public MenuNotFoundException(final Long menuId) {
        super(RESOURCE, menuId);
    }
}
