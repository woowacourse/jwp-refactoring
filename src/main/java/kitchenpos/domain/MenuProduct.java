package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Menu menu, final Product product, final long quantity) {
        this(null, menu, product, quantity);
    }

    public MenuProduct(final Long seq, final Menu menu, final Product product,
                       final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = new Quantity(quantity);
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
        return quantity.getValue();
    }
}
