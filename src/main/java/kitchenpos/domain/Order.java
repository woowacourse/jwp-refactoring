package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
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
import org.springframework.data.annotation.CreatedDate;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
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
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
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
