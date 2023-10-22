package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private final Menu menu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private final Product product;
    private final long quantity;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;

    private MenuProduct(final Long seq, final Menu menu, final Product product, final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(final Menu menu, final Product product, final long quantity) {
        this(null, menu, product, quantity);
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, null, product, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
