package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue
    private Long seq;

    @ManyToOne
    private Product product;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this(null, product, quantity);
    }

    private MenuProduct(Long seq, Product product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
