package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(final Menu menu, final Product product, final long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public void mapMenu(final Menu menu) {
        if (this.menu != null) {
            this.menu.getMenuProducts().remove(this);
        }
        this.menu = menu;
        menu.getMenuProducts().add(this);
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

    public BigDecimal getPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuProduct)) {
            return false;
        }
        final MenuProduct other = (MenuProduct) o;

        return Objects.equals(seq, other.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menu, product, quantity);
    }
}
