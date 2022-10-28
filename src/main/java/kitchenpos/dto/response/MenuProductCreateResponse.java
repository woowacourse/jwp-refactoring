package kitchenpos.dto.response;

public class MenuProductCreateResponse {

    private Long seq;
    private Long productId;
    private long quantity;

    private MenuProductCreateResponse() {
    }

    public MenuProductCreateResponse(final Long seq, final Long productId, final long quantity) {
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
