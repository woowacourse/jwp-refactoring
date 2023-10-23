package kitchenpos.domain;

import java.math.BigDecimal;
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
    private Product product;

    private long quantity;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    protected MenuProduct() {
    }

    public MenuProduct(final Long seq, final Product product, final long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct forSave(final Product product, final long quantity) {

        return new MenuProduct(null, product, quantity);
    }

    public void joinMenu(final Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getPrice() {
        final BigDecimal price = product.getPrice();
        final BigDecimal quantity = BigDecimal.valueOf(this.quantity);

        return price.multiply(quantity);
    }
}
