package kitchenpos.fixture;

import kitchenpos.application.menu.MenuGroupRequest;
import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupFactory {

    public static MenuGroup createMenuGroup() {
        return new MenuGroup("후라이드 치킨");
    }

    public static MenuGroup createMenuGroup(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroupRequest createMenuGroupRequest() {
        return new MenuGroupRequest("후라이드 치킨");
    }
}
