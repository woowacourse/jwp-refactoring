package kitchenpos.application.dto.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse();
    }
}
