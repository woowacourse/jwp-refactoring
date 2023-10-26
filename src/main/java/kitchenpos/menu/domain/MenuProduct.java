package kitchenpos.menu.domain;

import java.util.Optional;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.product.domain.Product;
import kitchenpos.vo.Price;
import kitchenpos.vo.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(
            final Long seq,
            final Product product,
            final Quantity quantity
    ) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(
            final Product product,
            final Long quantity
    ) {
        this(null, product, new Quantity(quantity));
    }

    public Price caculateTotalPrice() {
        return product.multiplyPrice(quantity.getValue());
    }

    public Long getSeq() {
        return seq;
    }

    public Optional<Long> getProductId() {
        if (product == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(product.getId());
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
