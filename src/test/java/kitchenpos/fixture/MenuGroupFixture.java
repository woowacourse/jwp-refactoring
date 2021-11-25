package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;

public class MenuGroupFixture {
    public static MenuGroup menuGroup() {
        return new MenuGroup(0L, "name");
    }

    public static MenuGroupRequest menuGroupRequest() {
        return new MenuGroupRequest("name");
    }
}
