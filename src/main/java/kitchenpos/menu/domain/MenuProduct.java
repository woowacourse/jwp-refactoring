package kitchenpos.menu.domain;

import static javax.persistence.FetchType.LAZY;

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

    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = LAZY)
    private Menu menu;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = LAZY)
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(long quantity) {
        this(null, null, null, quantity);
    }

    public MenuProduct(Product product, long quantity) {
        this(null, null, product, quantity);
    }

    public MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal priceSum() {

        final Product product = getProduct();
        final BigDecimal price = product.getPrice();

        return price.multiply(BigDecimal.valueOf(this.quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "seq=" + seq +
                ", quantity=" + quantity +
                '}';
    }
}
