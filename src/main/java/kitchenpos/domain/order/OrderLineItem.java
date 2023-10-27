package kitchenpos.domain.order;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;
    private Long menuId;
    private long quantity;
    private String name;
    private BigDecimal price;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final long quantity, final String name, final BigDecimal price) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
