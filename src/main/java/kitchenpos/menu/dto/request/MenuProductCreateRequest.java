package kitchenpos.menu.dto.request;

public class MenuProductCreateRequest {
    private Long productId;
    private long quantity;

    public MenuProductCreateRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long productId() {
        return productId;
    }

    public long quantity() {
        return quantity;
    }
}
