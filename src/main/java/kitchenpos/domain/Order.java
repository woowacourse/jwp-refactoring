package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "seq")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(
            final OrderTable orderTable,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItem> orderLineItems
    ) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public Order(
            final Long id,
            final OrderTable orderTable,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItem> orderLineItems
    ) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public void updateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
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

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
