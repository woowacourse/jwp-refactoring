package kitchenpos.menu.dto;

public class MenuProductRequest {
    private long productId;
    private long quantity;

    private MenuProductRequest(final long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest() {
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
