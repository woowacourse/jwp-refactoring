package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id", nullable = false, updatable = false, insertable = false)
    private Long menuId;

    @Column
    private Long productId;

    @Column
    private long quantity;

    @Transient
    private BigDecimal price;

    public MenuProduct() {
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(null, null, productId, quantity, null);
    }


    public MenuProduct(final Long menuId, final Long productId, final long quantity) {
        this(null, menuId, productId, quantity, null);
    }

    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this(null, null, productId, quantity, price);
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity,
                       final BigDecimal price) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
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

    public BigDecimal calculateAmount() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
