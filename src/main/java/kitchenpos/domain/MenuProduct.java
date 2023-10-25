package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Menu menu,
                       final Product product,
                       final long quantity) {
        this(null, menu, product, quantity);
    }

    public MenuProduct(final Long seq,
                       final Menu menu,
                       final Product product,
                       final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal calculateTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }
}
