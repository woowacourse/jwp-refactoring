package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 할인메뉴 = generate("할인메뉴");
    public static MenuGroup 세트메뉴 = generate("세트메뉴");

    private static MenuGroup generate(final String menuGroupName) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuGroupName);
        return menuGroup;
    }
}
