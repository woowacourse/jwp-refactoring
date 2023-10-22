package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this(null, product, quantity, menu);
    }

    public MenuProduct(Long seq, Product product, long quantity, Menu menu) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
        this.menu = menu;
    }

    public BigDecimal menuPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MenuProduct that = (MenuProduct) object;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
