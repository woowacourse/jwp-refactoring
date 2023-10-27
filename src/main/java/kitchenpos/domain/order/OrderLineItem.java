package kitchenpos.domain.order;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(nullable = false)
    private Long menuId;
    private long quantity;
    private String menuName;
    private BigDecimal menuPrice;

    public OrderLineItem(final Long menuId, final long quantity, final String menuName, final BigDecimal menuPrice) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    protected OrderLineItem() {
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

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}
