package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(
            final OrderTable orderTable,
            final OrderStatus orderStatus,
            final List<OrderLineItem> orderLineItems
    ) {
        validate(orderTable, orderLineItems);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = appendOrderLineItems(orderLineItems);
    }

    private void validate(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        validateEmptyOrderTable(orderTable);
        validateEmptyOrderLienItem(orderLineItems);
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블이 비어있습니다.");
        }
    }

    private void validateEmptyOrderLienItem(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("[ERROR] 주문 항목이 비어있습니다.");
        }
    }

    private List<OrderLineItem> appendOrderLineItems(final List<OrderLineItem> orderLineItems) {
        final List<OrderLineItem> returnOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(this);
            returnOrderLineItems.add(orderLineItem);
        }
        return returnOrderLineItems;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateOrderStatusNotCompletion();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatusNotCompletion() {
        if (Objects.equals(OrderStatus.COMPLETION, this.getOrderStatus())) {
            throw new IllegalArgumentException("[ERROR] 완료된 주문은 상태 변경이 불가능합니다.");
        }
    }

    public boolean isNotCompletion() {
        return this.orderStatus != OrderStatus.COMPLETION;
    }

    public void applyOrderLineItem(final List<OrderLineItem> orderLineItems) {
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
        return Collections.unmodifiableList(orderLineItems);
    }
}
