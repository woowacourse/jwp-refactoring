package kitchenpos.domain.menu;

import kitchenpos.domain.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private long quantity;

    public MenuProduct(final Product product, final long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    protected MenuProduct() {
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

    public BigDecimal calculatePriceMultiplyQuantity() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
