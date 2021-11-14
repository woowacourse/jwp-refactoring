package kitchenpos.menu.dto;

public class MenuProductRequest {

    private final Long seq;
    private final Long productId;
    private final Long quantity;

    public MenuProductRequest(Long seq, Long productId, Long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getSeq() {
        return seq;
    }
}
