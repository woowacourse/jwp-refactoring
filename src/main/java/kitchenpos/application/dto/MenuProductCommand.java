package kitchenpos.application.dto;

public class MenuProductCommand {

    private Long productId;
    private long quantity;

    public MenuProductCommand(Long productId, long quantity) {
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
