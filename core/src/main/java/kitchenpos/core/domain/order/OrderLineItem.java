package kitchenpos.core.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.core.domain.menu.MenuHistory;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column
    private Long orderId;

    @Column
    private Long menuHistoryId;

    @Column
    private Integer quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, Long orderId, Long menuHistoryId, Integer quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuHistoryId = menuHistoryId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long orderId, MenuHistory menuHistory, Integer quantity) {
        this(null, orderId, menuHistory.getId(), quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuHistoryId() {
        return menuHistoryId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
