package kitchenpos.application.dto.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private final Long productId;
    private final long quantity;

    public MenuProductResponse(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse toDto(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
