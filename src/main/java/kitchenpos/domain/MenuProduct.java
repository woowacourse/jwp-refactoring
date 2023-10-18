package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long quantity;

    public MenuProduct() {
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

    public static MenuProduct of(final Long seq,
                                 final Long menuId,
                                 final Long productId,
                                 final long quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }

    public static MenuProduct of(final Long menuId,
                                 final Long productId,
                                 final long quantity) {
        return new MenuProduct(null, menuId, productId, quantity);
    }

    public static MenuProduct of(final Long productId,
                                 final long quantity) {
        return new MenuProduct(null, null, productId, quantity);
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
