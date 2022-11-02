package kitchenpos.domain.menu;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;

@Table(name = "menu_product")
@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, product, quantity);
    }

    private MenuProduct(final Long seq, final Product product, final long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public Price calculateAmount() {
        return product.getPrice()
                .multiply(quantity);
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
}
