package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFactory {

    public static MenuGroup createMenuGroup() {
        return new MenuGroup("후라이드 치킨");
    }

    public static MenuGroup createMenuGroup(String name) {
        return new MenuGroup(name);
    }
}
