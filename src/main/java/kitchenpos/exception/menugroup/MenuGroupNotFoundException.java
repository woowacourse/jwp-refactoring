package kitchenpos.exception.menugroup;

import kitchenpos.exception.common.NotFoundException;

public class MenuGroupNotFoundException extends NotFoundException {
    private final static String MENU_GROUP = "메뉴 그룹";

    public MenuGroupNotFoundException(final long id) {
        super(MENU_GROUP, id);
    }
}
