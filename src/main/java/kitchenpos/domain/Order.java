package kitchenpos.domain;

import static kitchenpos.domain.vo.OrderStatus.COOKING;
import static kitchenpos.domain.vo.OrderStatus.MEAL;

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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.domain.vo.OrderStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(final OrderTable orderTable,
                 final List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        orderTable.placeOrder(this);
        this.orderStatus = COOKING;
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = new ArrayList<>(orderLineItems);
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(this));
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문할 항목은 최소 1개 이상이어야 합니다.");
        }
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public boolean isInProgress() {
        return MEAL.equals(orderStatus) || COOKING.equals(orderStatus);
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
