package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long id, Product product, Menu menu, long quantity) {
        this.id = id;
        this.product = product;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Price price() {
        return product.getPrice().multiply(quantity);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
