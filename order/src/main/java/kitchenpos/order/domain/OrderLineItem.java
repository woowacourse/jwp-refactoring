package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

public class OrderLineItem {

    @Id
    private Long seq;
    private AggregateReference<Menu, Long> menuId;
    private long quantity;

    private OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this(null, menuId, quantity);
    }

    private OrderLineItem(Long seq, Long menuId, long quantity) {
        this.seq = seq;
        this.menuId = AggregateReference.to(menuId);
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public AggregateReference<Menu, Long> getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

}
