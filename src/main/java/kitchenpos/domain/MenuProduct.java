package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class MenuProduct {

    private final Long id;
    private final Long menuId;
    private final Product product;
    private final long quantity;

    public MenuProduct(final Long id, final Long menuId, final Product product, final long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(final Long memberId, final Product product, final long quantity) {
        this(null, memberId, product, quantity);
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, null, product, quantity);
    }

    public BigDecimal calculateAmount() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }


    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
