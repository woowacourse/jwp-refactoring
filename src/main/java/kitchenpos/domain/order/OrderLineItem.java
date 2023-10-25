package kitchenpos.domain.order;

import javax.persistence.Embedded;
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
    private long quantity;

    @Embedded
    private OrderMenu orderMenu;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this(null, menuId, quantity);
    }

    public OrderLineItem(Long seq, Long menuId, long quantity) {
        this(seq, menuId, quantity, new OrderMenu());
    }

    public OrderLineItem(Long seq, Long menuId, long quantity, OrderMenu orderMenu) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.orderMenu = orderMenu;
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

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }
}
