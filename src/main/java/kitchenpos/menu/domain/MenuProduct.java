package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.product.domain.Product;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private long quantity;

    public MenuProduct(final Product product, final long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct() {
    }

    public BigDecimal calculateTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
