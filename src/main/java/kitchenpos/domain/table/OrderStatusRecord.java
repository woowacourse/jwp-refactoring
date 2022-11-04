package kitchenpos.domain.table;

import static kitchenpos.domain.common.OrderStatus.COMPLETION;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.common.OrderStatus;
import kitchenpos.exception.badrequest.CompletedOrderCannotChangeException;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "order_status_record")
public class OrderStatusRecord implements Persistable<Long> {

    @Id
    @Column(name = "order_id")
    private Long orderId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    protected OrderStatusRecord() {
    }

    public OrderStatusRecord(final Long orderId, final OrderTable orderTable, final OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        orderTable.add(this);
    }

    public boolean isNotCompleted() {
        return orderStatus != OrderStatus.COMPLETION;
    }

    public void changeOrderStatus(final String orderStatusName) {
        validateNotCompleted();
        this.orderStatus = OrderStatus.valueOf(orderStatusName);
    }

    private void validateNotCompleted() {
        if (orderStatus == COMPLETION) {
            throw new CompletedOrderCannotChangeException();
        }
    }

    @Override
    public Long getId() {
        return orderId;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderStatusRecord)) {
            return false;
        }
        OrderStatusRecord that = (OrderStatusRecord) o;
        return Objects.equals(orderId, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
