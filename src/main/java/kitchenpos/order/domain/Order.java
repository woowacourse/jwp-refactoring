package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    protected Order(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                    final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        addOrderLineItems(orderLineItems);
    }

    public static Order of(final Long orderTableId, final OrderStatus orderStatus,
                           final List<OrderLineItem> orderLineItems, final OrderValidator validator) {
        validator.validate(orderTableId, orderLineItems);
        return new Order(null, orderTableId, orderStatus, orderLineItems);
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrder(this);
        }
        this.orderLineItems = orderLineItems;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        checkCompleted();
        this.orderStatus = orderStatus;
    }

    private void checkCompleted() {
        if (completed()) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }
    }

    private boolean completed() {
        return Objects.equals(OrderStatus.COMPLETION, orderStatus);
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
