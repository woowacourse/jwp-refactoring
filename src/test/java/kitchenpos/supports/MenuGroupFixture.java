package kitchenpos.supports;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    private static final Long COUNT = 1L;
    private static final String DEFAULT_NAME = "기본 메뉴";

    public static MenuGroup create() {
        return new MenuGroup(DEFAULT_NAME + COUNT);
    }
}
