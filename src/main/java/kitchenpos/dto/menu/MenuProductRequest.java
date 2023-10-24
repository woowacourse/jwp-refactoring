package kitchenpos.dto.menu;

public class MenuProductRequest {
    private final long productId;
    private final long quantity;

    private MenuProductRequest(final long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(final long productId, final long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
