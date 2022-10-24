package kitchenpos.support.fixture.domain;

import kitchenpos.domain.MenuGroup;

public enum MenuGroupFixture {

    KOREAN("한식"),
    ;

    private final String name;

    MenuGroupFixture(String name) {
        this.name = name;
    }

    public MenuGroup getMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public MenuGroup getMenuGroup(Long id) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
