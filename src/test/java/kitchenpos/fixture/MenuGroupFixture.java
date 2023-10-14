package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴그룹_생성(String name) {
        return new MenuGroup(name);
    }
}
