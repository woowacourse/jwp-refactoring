package kitchenpos.test.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹(String name) {
        return new MenuGroup(name);
    }
}
