package kitchenpos.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, Quantity quantity) {
        this(null, menu, product, quantity);
    }

    public MenuProduct(Long seq, Menu menu, Product product, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
        menu.add(this);
    }

    public Price amount() {
        return product.calculateAmount(quantity);
    }

    public Long seq() {
        return seq;
    }

    public Menu menu() {
        return menu;
    }

    public Product product() {
        return product;
    }

    public Quantity quantity() {
        return quantity;
    }
}
