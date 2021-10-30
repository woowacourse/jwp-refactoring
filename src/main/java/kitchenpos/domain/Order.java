package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_order_table"), nullable = false)
    private OrderTable orderTable;

    @Column(nullable = false)
    private String orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        this(null, orderTable, orderStatus, orderedTime);
    }

    public Order(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
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

    public void changeOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
