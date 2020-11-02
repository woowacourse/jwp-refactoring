package kitchenpos.domain.order;

import kitchenpos.domain.BaseEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;


@Table(name = "`Order`")
@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID"))
@Entity
public class Order extends BaseEntity {
    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.COOKING;

    @OneToOne
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    public OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    @CreatedDate
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = DEFAULT_ORDER_STATUS;
        this.orderLineItems = orderLineItems;
    }

    public void updateOrderStatus(final String orderStatus) {
        if (OrderStatus.COMPLETION == this.orderStatus) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public void updateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
