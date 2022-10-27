package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.application.dto.MenuProductCreationDto;

public class MenuProduct {
    private Long id;
    private Long menuId;
    //    private Long productId;
    private Product product;
    private long quantity;

    public MenuProduct() {}

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

    @Deprecated
    public void setId(final Long id) {
        this.id = id;
    }

    public Long getMenuId() {
        return menuId;
    }

    @Deprecated
    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return product.getId();
    }

    @Deprecated
    public void setProductId(final Long productId) {
        if (product == null) {
            product = new Product();
        }
        this.product.setId(productId);
    }

    public long getQuantity() {
        return quantity;
    }

    @Deprecated
    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }
}
