package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    private String orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_line_item_id")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    public Order(String orderStatus) {
        this(null, null, OrderStatus.valueOf(orderStatus).name(), null, null);
    }

    public Long getId() {
        return id;
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

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void enrollId(Long id) {
        this.id = id;
    }

    public void enrollOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void changeStatus(String status) {
        this.orderStatus = status;
    }
}
