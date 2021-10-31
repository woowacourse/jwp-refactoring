package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {
    public static MenuGroup menuGroup() {
        return new MenuGroup(0L, "name");
    }
}
