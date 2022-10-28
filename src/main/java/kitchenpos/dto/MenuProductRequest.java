package kitchenpos.dto;

public class MenuProductRequest {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
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

    @Override
    public String toString() {
        return "MenuProductRequest{" +
                "seq=" + seq +
                ", menuId=" + menuId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
