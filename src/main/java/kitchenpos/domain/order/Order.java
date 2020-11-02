package kitchenpos.domain.order;

import kitchenpos.domain.BaseEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;


@Table(name = "`Order`")
@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID"))
@Entity
public class Order extends BaseEntity {
    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.COOKING;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    public OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    @CreatedDate
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
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

    public Long getOrderTableId() {
        return orderTableId;
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
