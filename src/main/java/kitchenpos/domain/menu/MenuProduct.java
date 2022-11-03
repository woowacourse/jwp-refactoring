package kitchenpos.domain.menu;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import kitchenpos.domain.Price;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    
    @Column(nullable = false)
    private Long productId;
    
    @Column(nullable = false)
    private long quantity;
    
    @Transient
    private Price price;

    protected MenuProduct() {
    }

    /**
     * DB 에 저장되지 않은 객체
     * Service 로직을 단순화하기 위해 Price 정보 추가
     */
    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this(null, productId, quantity, new Price(price));
    }

    /**
     * DB 에 저장하기 위한 객체
     */
    public MenuProduct(final Long productId, final long quantity) {
        this(null, productId, quantity, null);
    }

    /**
     * DB 에 저장된 객체
     */
    public MenuProduct(final Long seq, final Long productId, final long quantity) {
        this(seq, productId, quantity, null);
    }

    public MenuProduct(final Long seq, final Long productId, final long quantity, final Price price) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getAmount() {
        return price.multiply(quantity);
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

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
