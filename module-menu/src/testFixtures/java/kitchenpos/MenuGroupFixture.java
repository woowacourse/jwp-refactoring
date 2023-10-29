package kitchenpos;

import kitchenpos.menu.service.dto.MenuGroupRequest;

public class MenuGroupFixture {

    private static final Long COUNT = 1L;
    private static final String DEFAULT_NAME = "기본 메뉴";

    public static MenuGroupRequest create() {
        return new MenuGroupRequest(DEFAULT_NAME + COUNT);
    }
}
