package kitchenpos.ui.dto.response;

public class MenuProductResponse {

    private Long seq;
    private Long quantity;

    private MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, Long quantity) {
        this.seq = seq;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity;
    }
}
