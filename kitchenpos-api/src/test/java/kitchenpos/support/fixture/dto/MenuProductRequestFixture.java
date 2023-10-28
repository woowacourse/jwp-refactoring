package kitchenpos.support.fixture.dto;

import kitchenpos.application.menu.dto.MenuProductRequest;

public abstract class MenuProductRequestFixture {

    public static MenuProductRequest menuProductRequest(final Long productId, final long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
