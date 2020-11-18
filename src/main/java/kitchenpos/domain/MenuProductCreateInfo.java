package kitchenpos.domain;

public class MenuProductCreateInfo {
    private Long productId;
    private long quantity;

    private MenuProductCreateInfo() {
    }

    public MenuProductCreateInfo(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(productId, quantity);
    }
}
