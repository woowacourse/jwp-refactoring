package kitchenpos.menu.dto;

public class MenuProductResponse {

    private final Long seq;
    private final Long productId;
    private final Long quantity;

    public MenuProductResponse(
            final Long seq,
            final Long productId,
            final Long quantity
    ) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
