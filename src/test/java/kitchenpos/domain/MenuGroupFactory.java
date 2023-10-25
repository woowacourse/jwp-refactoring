package kitchenpos.domain;

import kitchenpos.domain.menugroup.MenuGroup;

public final class MenuGroupFactory {

    private MenuGroupFactory() {
    }

    public static MenuGroup createMenuGroupOf(final String name) {
        return new MenuGroup(name);
    }
}
