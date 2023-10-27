package kitchenpos.order.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.order.domain.service.OrderValidator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @CreatedDate
    private LocalDateTime orderedTime;

    protected Order() {
    }

    private Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static Order create(Long orderTableId, List<OrderLineItem> orderLineItems,
                               OrderValidator orderValidator) {
        Order order = new Order(orderTableId, OrderStatus.COOKING, orderLineItems);
        orderValidator.validate(order);
        return order;
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("이미 계산 완료된 주문입니다.");
        }
        this.orderStatus = orderStatus;
    }

    public boolean isAbleToUngroup() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public boolean isAbleToChangeEmpty() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
