package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.OrderStatus.COOKING;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    private Order(
            final OrderTable orderTable,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItem> orderLineItems
    ) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order ofCooking(final OrderTable orderTable, final Map<Menu, Long> menuWithQuantityMap) {
        orderTable.validateIsEmpty();
        final Order order = new Order(orderTable, COOKING, LocalDateTime.now());
        order.orderLineItems = menuWithQuantityMap.keySet().stream()
                .map(menu -> OrderLineItem.of(order, menu, menuWithQuantityMap.get(menu)))
                .collect(Collectors.toList());
        return order;
    }

    public void updateOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
