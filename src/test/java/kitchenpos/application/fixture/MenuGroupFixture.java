package kitchenpos.application.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 치킨() {
        return new MenuGroup("치킨");
    }

    public static MenuGroup 여러마리_메뉴_그룹() {
        return new MenuGroup("여러마리 메뉴");
    }

    public static MenuGroup 피자() {
        return new MenuGroup("피자");
    }
}
