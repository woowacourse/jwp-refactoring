package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long menuId;
    private Long productId;
    private BigDecimal price;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final BigDecimal price, final long quantity) {
        this(null, null, productId, price, quantity);
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long seq,
                       final Long menuId,
                       final Long productId,
                       final BigDecimal price,
                       final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public void addMenu(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
