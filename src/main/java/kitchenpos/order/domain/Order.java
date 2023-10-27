package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.common.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "orders")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(
            final List<OrderLineItem> orderLineItems,
            final OrderTable orderTable,
            final OrderStatus orderStatus
    ) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;

        for (final OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    public void changeOrderStatus(final OrderStatus orderStatusToChange) {
        validateOrderStatus();
        this.orderStatus = orderStatusToChange;
    }

    private void validateOrderStatus() {
        if (orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("결제완료 상태인 경우 주문상태를 변경할 수 없습니다.");
        }
    }

    public void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItem.setOrder(this);
        this.orderLineItems.add(orderLineItem);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
