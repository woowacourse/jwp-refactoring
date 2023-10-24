package kitchenpos.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime) {
        this(orderTable, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        validate(orderTable, orderLineItems);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        for (final OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItems(orderLineItem);
        }
    }

    private void validate(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        validateEmptyOrderTable(orderTable);
        validateEmptyOrderLineItems(orderLineItems);
    }

    private void validateEmptyOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void addOrderLineItems(final OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
        if (orderLineItem.getOrder() != this) {
            orderLineItem.setOrder(this);
        }
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException();
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
