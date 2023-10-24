package kitchenpos.domain;

import javax.persistence.Entity;
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

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Menu menu, final long quantity, final Product product) {
        this.menu = menu;
        this.quantity = quantity;
        this.product = product;
    }

    public void addToMenu(final Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return product.getId();
    }
}
