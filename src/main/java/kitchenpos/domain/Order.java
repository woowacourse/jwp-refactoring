package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    
    @CreatedDate
    private LocalDateTime orderedTime;
    
    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;
    
    public Order(final OrderTable orderTable,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }
    
    public Order(final Long id,
                 final OrderTable orderTable,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
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
}
