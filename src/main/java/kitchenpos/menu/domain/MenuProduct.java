package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

public class MenuProduct {

    private final Long id;
    private Long menuId;
    private final Long productId;
    private long quantity;

    public MenuProduct(Long id, Long menuId, Long productId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long menuId, Long productId, long quantity) {
        this(null, menuId, productId, quantity);
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, productId, quantity);
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

    public Price calculateTotalPrice(Price price) {
        return price.multiply(quantity);
    }
}
