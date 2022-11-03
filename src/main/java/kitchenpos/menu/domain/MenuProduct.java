package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long productId;
    private long quantity;
    @Column(name = "menu_id", insertable = false, updatable = false)
    private Long menuId;

    protected MenuProduct() {
    }

    public MenuProduct(final Long seq, final Long productId, final Long quantity, final Long menuId) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.menuId = menuId;
    }

    public MenuProduct(final Long productId, final long quantity, final Long menuId) {
        this(null, productId, quantity, menuId);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }
}
