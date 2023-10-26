package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import static java.util.Objects.isNull;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @OneToOne
    private Product product;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long seq, final Product product, final long quantity) {
        if (isNull(product)) {
            throw new IllegalArgumentException("상품이 존재해야 합니다.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("상품의 개수는 양수여야 합니다.");
        }
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, product, quantity);
    }

    public Price calculatePrice() {
        return product.getPrice().multiply(quantity);
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
