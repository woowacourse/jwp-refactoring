package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹(final Long id, final String name) {
        return new MenuGroup(id, name);
    }
}
