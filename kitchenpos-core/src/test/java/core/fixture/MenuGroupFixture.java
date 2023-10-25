package core.fixture;

import core.application.dto.MenuGroupRequest;
import core.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup menuGroup(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroupRequest menuGroupRequest(String name) {
        return new MenuGroupRequest(name);
    }
}
