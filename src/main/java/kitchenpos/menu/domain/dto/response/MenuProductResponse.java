package kitchenpos.menu.domain.dto.response;

public class MenuProductResponse {

    private Long seq;
    private long productId;
    private long quantity;

    private MenuProductResponse(Long seq, long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    protected MenuProductResponse() {
    }

    public Long getSeq() {
        return seq;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public static MenuProductResponse toDTO(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getQuantity());
    }
}
