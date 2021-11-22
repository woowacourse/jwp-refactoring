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
import javax.persistence.OneToMany;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    private String orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Orders() {
    }

    public Orders(Long orderTableId) {
        this(null, orderTableId, null, null);
    }

    public Orders(Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime);
    }

    public Orders(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public void changeStatus(String status) {
        this.orderStatus = status;
    }

    public void add(List<OrderLineItem> sources) {
        orderLineItems.addAll(sources);
        for (OrderLineItem source : sources) {
            source.belongsTo(this);
        }
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
}
