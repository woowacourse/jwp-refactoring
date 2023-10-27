package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.vo.Price;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "product_id")
    private Long productId;

    @Column
    private long quantity;

    protected MenuProduct() {}

    public MenuProduct(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Price calculateMenuPrice(final Price singlePrice) {
        return singlePrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
