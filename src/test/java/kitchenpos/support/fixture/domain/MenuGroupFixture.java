package kitchenpos.support.fixture.domain;

import kitchenpos.menu.domain.MenuGroup;

public enum MenuGroupFixture {

    KOREAN("한식"),
    ;

    private final String name;

    MenuGroupFixture(String name) {
        this.name = name;
    }

    public MenuGroup getMenuGroup() {
        return new MenuGroup(name);
    }

    public MenuGroup getMenuGroup(Long id) {
        return new MenuGroup(id, name);
    }
}
