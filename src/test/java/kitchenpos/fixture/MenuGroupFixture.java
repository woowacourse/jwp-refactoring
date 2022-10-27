package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 할인메뉴 = generate("할인메뉴");
    public static MenuGroup 세트메뉴 = generate("세트메뉴");
    public static MenuGroup 한마리메뉴 = generate("한마리메뉴");
    public static MenuGroup 한마리메뉴_1L = generateWithId("한마리메뉴", 1L);

    public static MenuGroup generateMenuGroup(final String menuGroupName) {
        return new MenuGroup(menuGroupName);
    }

    public static MenuGroup generateMenuGroupWithId(final String menuGroupName, final Long id) {
        return new MenuGroup(id, menuGroupName);
    }

    private static MenuGroup generate(final String menuGroupName) {
        return new MenuGroup(menuGroupName);
    }

    private static MenuGroup generateWithId(final String menuGroupName, Long id) {
        return new MenuGroup(id, menuGroupName);
    }
}
