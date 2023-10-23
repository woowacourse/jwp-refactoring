package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.product.Product;
import kitchenpos.support.domain.BaseEntity;
import kitchenpos.support.money.Money;

@Entity
public class MenuProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this(null, product, quantity);
    }

    public MenuProduct(Long seq, Product product, long quantity) {
        validate(product);
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    private void validate(Product product) {
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException("상품이 존재하지 않습니다.");
        }
    }

    public Money calculateAmount() {
        return product.getPrice().times(quantity);
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
