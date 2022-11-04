package kitchenpos.menu.ui.response;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuProductResponse {

    private final long seq;
    private final long productId;
    private final long quantity;

    @JsonCreator
    public MenuProductResponse(final Long seq, final long productId, final long quantity) {
        System.out.println(seq +" " + productId + " " + quantity);
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getSeq() {
        return seq;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
