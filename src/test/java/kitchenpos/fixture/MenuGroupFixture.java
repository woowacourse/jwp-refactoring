package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static final String NAME = "DD";

    public static MenuGroup createWithoutId() {
        return new MenuGroup(null, NAME);
    }

    public static MenuGroup createWithId(Long id) {
        return new MenuGroup(id, NAME);
    }
}
