package kitchenpos.domain;

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

    protected Orders() {
    }

    public Orders(String orderStatus, LocalDateTime orderedTime) {
        this(null, orderStatus, orderedTime, null);
    }

    public Orders(
        Long id,
        String orderStatus,
        LocalDateTime orderedTime,
        OrderTable orderTable
    ) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTable = orderTable;
    }

    public void changeStatus(String status) {
        this.orderStatus = status;
    }

    public void add(List<OrderLineItem> sources) {
        for (OrderLineItem source : sources) {
            source.belongsTo(this);
        }
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

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
