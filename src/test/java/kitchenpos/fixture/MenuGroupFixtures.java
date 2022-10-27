package kitchenpos.fixture;

import kitchenpos.dto.request.MenuGroupCreateRequest;

public class MenuGroupFixtures {

    public static MenuGroupCreateRequest createMenuGroup(final String name) {
        return new MenuGroupCreateRequest(name);
    }

    public static MenuGroupCreateRequest 두마리메뉴() {
        return createMenuGroup("두마리메뉴");
    }

    public static MenuGroupCreateRequest 한마리메뉴() {
        return createMenuGroup("한마리메뉴");
    }
}
