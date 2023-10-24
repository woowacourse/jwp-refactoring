package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
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
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "menu_product_menu"))
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "menu_product_product"))
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Product product, final long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal calculateMenuProductPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
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

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
