package kitchenpos.fixture;

import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup menuGroup(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroupRequest menuGroupRequest(String name) {
        return new MenuGroupRequest(name);
    }
}
