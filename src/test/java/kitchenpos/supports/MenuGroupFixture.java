package kitchenpos.supports;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    private static final Long COUNT = 1L;
    private static final String DEFAULT_NAME = "기본 메뉴";

    public static MenuGroup create() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(DEFAULT_NAME + COUNT);
        return menuGroup;
    }
}
