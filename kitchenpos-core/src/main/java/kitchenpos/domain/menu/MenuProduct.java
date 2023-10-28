package kitchenpos.domain.menu;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.product.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "menu_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_menu_id"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_product_id"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, null, product, quantity);
    }

    public MenuProduct(final Long seq, final Menu menu, final Product product, final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal totalPrice() {
        return this.product.multiplyPrice(BigDecimal.valueOf(this.quantity));
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

    public long quantity() {
        return quantity;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }
}
