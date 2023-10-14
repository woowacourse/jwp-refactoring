package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtures {

    public static final MenuGroup 한마리_메뉴 = create("한마리메뉴");
    public static final MenuGroup 두마리_메뉴 = create("두마리메뉴");

    private static MenuGroup create(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

}
