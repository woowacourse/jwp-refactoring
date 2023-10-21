package kitchenpos.domain.menuproduct;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Product;

@Entity
public class MenuProduct {

    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Id
    private Long seq;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Embedded
    private MenuProductQuantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Product product, final Long quantity) {
        this(null, product, quantity);
    }

    public MenuProduct(final Long seq, final Product product, final Long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = new MenuProductQuantity(quantity);
    }

    public BigDecimal getPrice() {
        return product.getPrice()
                      .multiply(BigDecimal.valueOf(quantity.getValue()));
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }
}
