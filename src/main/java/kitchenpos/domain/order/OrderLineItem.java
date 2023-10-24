package kitchenpos.domain.order;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String menuName;

    private BigDecimal menuPrice;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final String menuName, final BigDecimal menuPrice, final long quantity) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
