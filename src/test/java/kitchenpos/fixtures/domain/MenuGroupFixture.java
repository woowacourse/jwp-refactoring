package kitchenpos.fixtures.domain;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;

public class MenuGroupFixture {

    public static MenuGroup createMenuGroup(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }

    public static MenuGroupRequest createMenuGroupRequest() {
        return createMenuGroupRequest("분식");
    }

    public static MenuGroupRequest createMenuGroupRequest(final String name) {
        return new MenuGroupRequest(name);
    }
}
