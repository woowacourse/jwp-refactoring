package kitchenpos.support.fixture.dto;

import kitchenpos.application.dto.menu.MenuGroupCreateRequest;

public class MenuGroupCreateRequestFixture {

    public static MenuGroupCreateRequest menuGroupCreateRequest(final String name) {
        return new MenuGroupCreateRequest(name);
    }
}
