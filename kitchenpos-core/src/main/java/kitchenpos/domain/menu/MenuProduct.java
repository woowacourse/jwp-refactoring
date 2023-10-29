package kitchenpos.domain.menu;

import kitchenpos.domain.product.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;

import java.util.Objects;

public class MenuProduct {
    @Id
    private Long seq;
    @Column("PRODUCT_ID")
    private AggregateReference<Product, Long> product;
    private long quantity;

    public MenuProduct(Long productId, long quantity) {
        this(null, productId, quantity);
    }

    @PersistenceCreator
    public MenuProduct(Long seq, Long product, long quantity) {
        this.seq = seq;
        this.product = AggregateReference.to(product);
        this.quantity = quantity;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MenuProduct that = (MenuProduct) object;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
