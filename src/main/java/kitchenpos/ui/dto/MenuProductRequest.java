package kitchenpos.ui.dto;

public class MenuProductRequest {
    private Long seq;
    private Long productId;
    private long quantity;

    public MenuProductRequest(Long seq, Long productId, long quantity) {
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

    public long getQuantity() {
        return quantity;
    }
}
