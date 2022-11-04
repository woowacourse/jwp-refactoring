package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final OrderLineItems orderLineItems) {
        validate(orderLineItems);
        validate(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems.arrangeOrder(this);
    }

    private void validate(final OrderLineItems orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 비어있을 수 없습니다.");
        }
        if (orderLineItems.hasDuplicate()) {
            throw new IllegalArgumentException("주문 항목엔 중복되는 메뉴가 존재할 수 없습니다.");
        }
    }

    private void validate(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비활성화된 주문 테이블은 주문을 받을 수 없습니다.");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException("주문이 이미 계산 완료되었습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public static class Builder {

        private Long id;
        private OrderTable orderTable;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private OrderLineItems orderLineItems;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder orderTable(final OrderTable orderTable) {
            this.orderTable = orderTable;
            return this;
        }

        public Builder orderStatus(final OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderedTime(final LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Builder orderLineItems(final OrderLineItems orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Builder orderLineItems(final List<OrderLineItem> orderLineItems) {
            this.orderLineItems = new OrderLineItems(orderLineItems);
            return this;
        }

        public Order build() {
            return new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);
        }
    }
}
