package kitchenpos.order.domain;

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
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private Long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final String menuName, final BigDecimal menuPrice, final Long quantity) {
        this(null, menuId, menuName, menuPrice, quantity);
    }

    public OrderLineItem(final Long seq,
                         final Long menuId,
                         final String menuName,
                         final BigDecimal menuPrice,
                         final Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public Long getQuantity() {
        return quantity;
    }
}
