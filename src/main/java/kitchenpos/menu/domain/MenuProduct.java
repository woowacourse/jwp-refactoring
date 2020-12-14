package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public MenuProduct() {
    }

    public MenuProduct(final Long seq, final Product product, final long quantity, final Menu menu) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
        this.menu = menu;
    }

    public MenuProduct(final Long seq, final Product product, final long quantity) {
        this(seq, product, quantity, null);
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, product, quantity);
    }

    public BigDecimal getTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
