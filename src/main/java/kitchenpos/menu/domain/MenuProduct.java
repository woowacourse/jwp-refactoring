package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProduct {

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
