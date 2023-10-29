package menu.exception;

import common.exception.NotFoundException;

public class MenuNotFoundException extends NotFoundException {
    private static final String RESOURCE = "메뉴";

    public MenuNotFoundException() {
        super(RESOURCE);
    }

    public MenuNotFoundException(final Long menuId) {
        super(RESOURCE, menuId);
    }
}
