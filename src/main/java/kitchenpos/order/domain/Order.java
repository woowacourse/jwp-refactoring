package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "orders")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @CreatedDate
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(final Long id, final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems,
                 final Long orderTableId) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderLineItems = new OrderLineItems(orderLineItems);
        this.orderTableId = orderTableId;
    }

    public static Order forSave(final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems,
                                final Long orderTableId) {
        return new Order(null, orderStatus, orderLineItems, orderTableId);
    }

    public void registerOrderTable(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("주문이 완료된 상태는 변경할 수 없습니다.");
        }
    }

    public boolean isProceeding() {
        return orderStatus != OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }
}
