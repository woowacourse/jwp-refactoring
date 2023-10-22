package kitchenpos.domain;

import org.hibernate.annotations.Tables;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.OrderStatus.COMPLETION;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void updateOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void validateStatusIsEqualTo() {
        if (Objects.equals(this.orderStatus, COMPLETION)) {
            throw new IllegalArgumentException("이미 완료된 주문의 상태는 변경할 수 없습니다!");
        }
    }

}
