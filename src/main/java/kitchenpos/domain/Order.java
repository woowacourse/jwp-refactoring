package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long orderTableId;

    @Column
    private String orderStatus;

    @Column
    private LocalDateTime orderedTime = LocalDateTime.now();

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public static Order forSave(final Long orderTableId) {
        return new Order(null, orderTableId, OrderStatus.COOKING.name());
    }

    public void addOrderLineItems(final OrderLineItem orderLineItem) {
        orderLineItem.setOrder(this);
        orderLineItems.add(orderLineItem);
    }

    public void changeStatus(final String changingStatus) {
        if (orderStatus.equals(OrderStatus.COMPLETION.name())) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = changingStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
