package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.common.vo.Money;

@Table(name = "menu_product")
@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "product_id")
    private Long productId;

    private long quantity;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "product_price_snapshot"))
    private Money productPriceSnapshot;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity, Money productPriceSnapshot) {
        this.productId = productId;
        this.quantity = quantity;
        this.productPriceSnapshot = productPriceSnapshot;
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
        return productPriceSnapshot.getValue();
    }
}
