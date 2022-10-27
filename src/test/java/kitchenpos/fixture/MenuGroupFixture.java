package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;

public class MenuGroupFixture {

    public static MenuGroupRequest 할인메뉴_REQUEST = generateMenuGroupRequest("할인메뉴");
    public static MenuGroupRequest 세트메뉴_REQUEST = generateMenuGroupRequest("세트메뉴");

    public static MenuGroup 할인메뉴 = generate("할인메뉴");
    public static MenuGroup 세트메뉴 = generate("세트메뉴");
    public static MenuGroup 한마리메뉴 = generate("한마리메뉴");
    public static MenuGroup 한마리메뉴_1L = generateWithId("한마리메뉴", 1L);

    private static MenuGroupRequest generateMenuGroupRequest(String menuGroupName) {
        return new MenuGroupRequest(menuGroupName);
    }

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
