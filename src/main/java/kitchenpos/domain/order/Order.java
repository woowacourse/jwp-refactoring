package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems;

    public Order(final Long id,
                 final Long orderTableId,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order ofNew(final OrderTable orderTable) {
        checkTableNotEmpty(orderTable);
        final Order order = new Order(null, orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now());
        return order;
    }

    public void changeOrderLineItems(final List<OrderLineItem> orderLineItems) {
        checkItemsNotEmpty(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isNotChangeable()) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    private void checkItemsNotEmpty(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private static void checkTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    protected Order() {
    }
}
