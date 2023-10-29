package kitchenpos.support.fixture.dto;

import kitchenpos.application.menugroup.dto.MenuGroupCreateRequest;

public abstract class MenuGroupCreateRequestFixture {

    public static MenuGroupCreateRequest menuGroupCreateRequest(final String name) {
        return new MenuGroupCreateRequest(name);
    }
}
