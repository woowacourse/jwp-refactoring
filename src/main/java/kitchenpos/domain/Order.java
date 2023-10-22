package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
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

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(final Long id, final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public static Order forSave(final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        return new Order(null, orderStatus, orderLineItems);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateOrderStatus(orderStatus);
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus(final OrderStatus orderStatus) {
        if (orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("주문이 완료된 상태는 변경할 수 없습니다.");
        }
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }
}
