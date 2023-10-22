package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private final LocalDateTime orderedTime = LocalDateTime.now();

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(final Long orderTableId, final String orderStatus) {
        this(null, orderTableId, orderStatus);
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Order(final Long id,
                 final Long orderTableId,
                 final String orderStatus,
                 final List<OrderLineItem> orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }
}
