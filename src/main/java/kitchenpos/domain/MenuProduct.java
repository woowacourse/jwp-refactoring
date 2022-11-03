package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long menuId, final Product product, final long quantity) {
        this(null, menuId, product, quantity);
    }

    public MenuProduct(final Long seq, final Long menuId, final Product product, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

    public Price calculateProductTotalPrice() {
        return this.product.multiplyPriceWith(this.quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
