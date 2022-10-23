package kitchenpos.domain;

public class MenuProduct {

    private Long id;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct(final Long id, final Long menuId, final Long productId, final long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
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

    public void addMenuId(final Long menuId) {
        this.menuId = menuId;
    }
}
