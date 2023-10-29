package kitchenpos.order;

import kitchenpos.menu.MenuHistory;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @Embedded
    private MenuHistory menuHistory;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity, MenuHistoryRecorder menuHistoryRecorder) {
        this.seq = null;
        this.menuHistory = menuHistoryRecorder.record(menuId);
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public MenuHistory getMenuHistory() {
        return menuHistory;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuHistory.getMenuId();
    }
}
