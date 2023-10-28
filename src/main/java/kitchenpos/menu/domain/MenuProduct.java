package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Long productId;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Menu menu, final Long productId, final long quantity) {
        this(null, menu, productId, quantity);
    }

    private MenuProduct(final Long seq, final Menu menu, final Long productId, final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
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
}
