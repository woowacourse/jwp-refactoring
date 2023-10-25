package kitchenpos.order.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static org.springframework.util.CollectionUtils.isEmpty;

@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Order {

    private static final OrderStatus INITIAL_ORDER_STATUS = OrderStatus.COOKING;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @JoinColumn(name = "order_id")
    @OneToMany(cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                 final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        if (isNull(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블이 없거나 빈 주문 테이블입니다.");
        }
        if (isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 필요합니다.");
        }
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, INITIAL_ORDER_STATUS, null, orderLineItems);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION == this.orderStatus) {
            throw new IllegalStateException("완료된 주문은 변경할 수 없습니다.");
        }

        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
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
