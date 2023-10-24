package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long menuId;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long menuId, final long quantity, final Product product) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return product.getId();
    }
}
