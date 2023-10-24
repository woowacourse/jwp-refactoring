package kitchenpos.application.menu.request;

public class MenuProductRequest {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductRequest(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest(Long productId, long quantity) {
        this(null, null, productId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
