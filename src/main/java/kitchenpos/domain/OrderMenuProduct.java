package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_menu_product")
public class OrderMenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "order_menu_id")
    private Long orderMenuId;

    @Column(name = "order_product_name")
    private String productName;

    @Column(name = "order_product_price")
    private BigDecimal productPrice;

    private long quantity;

    protected OrderMenuProduct() {
    }

    public OrderMenuProduct(final Long seq, final Long orderMenuId, final String productName,
                            final BigDecimal productPrice, final long quantity) {
        this.seq = seq;
        this.orderMenuId = orderMenuId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderMenuId() {
        return orderMenuId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
