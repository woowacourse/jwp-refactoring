package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long menuId, final Long productId, final long quantity) {
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

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
