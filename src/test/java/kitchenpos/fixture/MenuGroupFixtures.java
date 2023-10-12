package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtures {

    public static final MenuGroup 두마리_메뉴 = create("두마리메뉴");

    private static MenuGroup create(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(null);
        menuGroup.setName(name);
        return menuGroup;
    }

}
