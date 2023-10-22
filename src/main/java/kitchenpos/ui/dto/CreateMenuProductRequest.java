package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;

public class CreateMenuProductRequest {

    private Long productId;
    private long quantity;

    public CreateMenuProductRequest() {
    }

    public CreateMenuProductRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }
}
