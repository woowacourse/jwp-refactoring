package kitchenpos.support.fixtures;

import kitchenpos.domain.MenuGroup;

public enum MenuGroupFixtures {

    TWO_CHICKEN_GROUP("두마리 치킨 메뉴");

    private final String name;

    MenuGroupFixtures(final String name) {
        this.name = name;
    }

    public MenuGroup create() {
        return new MenuGroup(null, name);
    }
}
