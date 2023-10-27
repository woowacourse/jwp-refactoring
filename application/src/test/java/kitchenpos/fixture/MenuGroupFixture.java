package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 인기_메뉴_생성() {
        MenuGroup menuGroup = new MenuGroup("인기 메뉴");
        return menuGroup;
    }

}
