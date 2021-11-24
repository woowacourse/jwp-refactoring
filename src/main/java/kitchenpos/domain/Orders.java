package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderStatus;

    private LocalDateTime orderedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Orders() {
    }

    public Orders(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(null, COOKING.name(), LocalDateTime.now(), orderTable, orderLineItems);
    }

    public Orders(
        Long id,
        String orderStatus,
        LocalDateTime orderedTime,
        OrderTable orderTable,
        List<OrderLineItem> orderLineItems
    ) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;

        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.belongsTo(this);
        }
    }

    public void changeOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
