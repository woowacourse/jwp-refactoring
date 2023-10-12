package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴그룹_생성() {
        return new MenuGroup("메뉴그룹");
    }

    public static MenuGroup 메뉴그룹_생성(final String name) {
        return new MenuGroup(name);
    }
}
