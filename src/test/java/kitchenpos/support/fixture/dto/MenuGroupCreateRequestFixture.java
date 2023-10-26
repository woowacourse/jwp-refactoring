package kitchenpos.support.fixture.dto;

import kitchenpos.menugroup.application.dto.MenuGroupCreateRequest;

public abstract class MenuGroupCreateRequestFixture {

    public static MenuGroupCreateRequest menuGroupCreateRequest(final String name) {
        return new MenuGroupCreateRequest(name);
    }
}
