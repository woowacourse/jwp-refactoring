package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(final long menuId, final long productId, final int quantity) {
        this(null, menuId, productId, quantity);
    }

    public MenuProduct(final Long seq,
                       final Long menuId,
                       final Long productId,
                       final long quantity) {
        this.seq = seq;
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

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
