package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtures {

    public static final MenuGroup generateMenuGroup(final String name) {
        return generateMenuGroup(null, name);
    }

    public static final MenuGroup generateMenuGroup(final Long id, final MenuGroup menuGroup) {
        return generateMenuGroup(id, menuGroup.getName());
    }

    public static final MenuGroup generateMenuGroup(final Long id, final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
