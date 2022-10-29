package kitchenpos.dto.request;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    private MenuProductRequest() {
    }

    public MenuProductRequest(final MenuProduct menuProduct) {
        this(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public MenuProductRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
