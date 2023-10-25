package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private long quantity;

    public MenuProduct(final Menu menu, final Product product, final long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    protected MenuProduct() {
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

    public BigDecimal calculatePriceMultiplyQuantity() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
