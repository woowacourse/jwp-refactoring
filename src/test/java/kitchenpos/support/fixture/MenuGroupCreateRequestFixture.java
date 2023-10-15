package kitchenpos.support.fixture;

import kitchenpos.ui.dto.MenuGroupCreateRequest;

public class MenuGroupCreateRequestFixture {

    public static MenuGroupCreateRequest menuGroupCreateRequest(final String name) {
        return new MenuGroupCreateRequest(name);
    }
}
