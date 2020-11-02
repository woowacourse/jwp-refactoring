package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }

    public static MenuProduct entityOf(Product product, long quantity) {
        return new MenuProduct(null, null, product, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        if (Objects.isNull(this.menu)) {
            this.menu = menu;
        }
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getProductPrice() {
        return this.product.getPrice();
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
            "seq=" + seq +
            ", quantity=" + quantity +
            '}';
    }
}
