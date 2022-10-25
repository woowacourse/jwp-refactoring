package kitchenpos.domain;

public class MenuProduct {

    private final Long seq;
    private Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        validQuantity(quantity);
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

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    private void validQuantity(long quantity) {
        if(quantity <= 0){
            throw new IllegalArgumentException("0 이하의 양을 등록할 수 없습니다.");
        }
    }
}
