package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menu.vo.Name;
import kitchenpos.menu.vo.Price;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long menuId;

    private Name menuName;

    private Price menuPrice;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Long seq,
                         final Long menuId,
                         final String menuName,
                         final BigDecimal menuPrice,
                         final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuName = new Name(menuName);
        this.menuPrice = new Price(menuPrice);
        this.quantity = quantity;
    }

    public OrderLineItem(final Long menuId, final String menuName, final BigDecimal menuPrice, final long quantity) {
        this(null, menuId, menuName, menuPrice, quantity);
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
}
