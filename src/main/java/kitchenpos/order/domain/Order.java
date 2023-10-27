package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(
            final Long orderTableId,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime
    ) {
        this(null, orderTableId, orderStatus, orderedTime);
    }

    private Order(
            final Long id,
            final Long orderTableId,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order createBy(final Long orderTableId) {
        if (orderTableId == null) {
            throw new IllegalArgumentException("주문 생성시 주문 테이블은 비어있을 수 없습니다");
        }
        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now());
    }

    public void updateStatus(final String status) {
        if (this.orderStatus.isCompleted()) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }
        this.orderStatus = OrderStatus.valueOf(status);
    }

    public void validateUncompleted() {
        if (!orderStatus.isCompleted()) {
            throw new IllegalArgumentException("완료되지 않은 주문입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
