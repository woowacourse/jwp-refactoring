package kitchenpos.test.fixtures;

import kitchenpos.domain.MenuGroup;

public enum MenuGroupFixtures {
    EMPTY("empty"),
    BASIC("basic");

    private final String name;

    MenuGroupFixtures(final String name) {
        this.name = name;
    }

    public MenuGroup get() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
