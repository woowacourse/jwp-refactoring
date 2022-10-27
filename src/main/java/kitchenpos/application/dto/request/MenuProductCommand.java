package kitchenpos.application.dto.request;

public class MenuProductCommand {

    private final Long productId;
    private final long quantity;

    public MenuProductCommand(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
