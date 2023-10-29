package kitchenpos;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtures {

    private MenuGroupFixtures() {
    }

    public static MenuGroup from(final String name) {
        return new MenuGroup(name);
    }
}
