package kitchenpos.menu.application.dto.request;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductCommand {

    private final Long productId;
    private final long quantity;

    public MenuProductCommand(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(productId, quantity);
    }
}
