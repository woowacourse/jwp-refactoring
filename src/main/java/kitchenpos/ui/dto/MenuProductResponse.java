package kitchenpos.ui.dto;

public class MenuProductResponse {

    private final Long seq;
    private final ProductResponse productResponse;
    private final long quantity;

    public MenuProductResponse(final Long seq, final ProductResponse productResponse, final long quantity) {
        this.seq = seq;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
