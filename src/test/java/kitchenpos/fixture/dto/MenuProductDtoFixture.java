package kitchenpos.fixture.dto;

import kitchenpos.ui.dto.request.MenuProductCreateRequest;

public class MenuProductDtoFixture {

    public static MenuProductCreateRequest createMenuProductCreateRequest(final Long productId, final long quantity) {
        return new MenuProductCreateRequest(productId, quantity);
    }
}
