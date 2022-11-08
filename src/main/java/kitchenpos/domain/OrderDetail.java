package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import kitchenpos.exception.OrderTableUnableUngroupingStatusException;

@Entity
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    protected OrderDetail() {
    }

    public OrderDetail(Order order, OrderTable orderTable) {
        this.order = order;
        this.orderTable = orderTable;
    }

    public void ungrouping() {
        validateStatus();
        orderTable.ungrouping();
    }

    private void validateStatus() {
        if (order.isUnableToUngrouping()) {
            throw new OrderTableUnableUngroupingStatusException();
        }
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
