package menugroups.exception;


import common.exception.NotFoundException;

public class MenuGroupNotFoundException extends NotFoundException {
    private final static String MENU_GROUP = "메뉴 그룹";

    public MenuGroupNotFoundException(final long id) {
        super(MENU_GROUP, id);
    }
}
