package kitchenpos.application.dto.request;

import kitchenpos.domain.MenuProduct;

public record MenuProductCommand(Long productId, long quantity) {

    public MenuProduct toEntity() {
        return new MenuProduct(null, productId, quantity);
    }
}
