package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "quantity")
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(final Menu menu,
                       final Product product,
                       final long quantity
    ) {
        this(null, menu, product, quantity);
    }

    public MenuProduct(final Long seq,
                       final Menu menu,
                       final Product product,
                       final long quantity
    ) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
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
