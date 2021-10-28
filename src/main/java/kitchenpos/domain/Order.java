package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.KitchenException;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Column(nullable = false)
    private String orderStatus = OrderStatus.COOKING.name();

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(OrderTable orderTable,
        LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderedTime = orderedTime;
        validate();
    }

    public Order(Long id, OrderTable orderTable, String orderStatus,
        LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validate() {
        if (orderTable.isEmpty()) {
            throw new KitchenException("배정된 테이블은 빈 테이블입니다.");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isCompleted()) {
            throw new KitchenException("이미 완료된 주문입니다.");
        }
        this.orderStatus = orderStatus.name();
    }

    public boolean hasCookingOrMeal() {
        return !isCompleted();
    }

    public boolean isCompleted() {
        return this.orderStatus.equals(OrderStatus.COMPLETION.name());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
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
