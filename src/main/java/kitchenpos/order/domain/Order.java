package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Embedded
    private OrderLineItems orderLineItems;

    @CreatedDate
    @NotNull
    private LocalDateTime orderedTime;

    protected Order() {
    }

    private Order(
            OrderStatus orderStatus,
            OrderTable orderTable,
            OrderLineItems orderLineItems,
            OrderValidator orderValidator
    ) {
        validate(orderTable, orderLineItems);
        orderValidator.validateOrderLineItems(orderLineItems);

        this.orderStatus = orderStatus;
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
    }

    private void validate(OrderTable orderTable, OrderLineItems orderLineItems) {
        validateOrderTable(orderTable);
        validateOrderLineItems(orderLineItems);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable == null) {
            throw new NullPointerException("주문 테이블은 null일 수 없습니다.");
        }

        if (Boolean.TRUE.equals(orderTable.isEmpty())) {
            throw new IllegalArgumentException("주문할 수 없는 상태의 테이블이 존재합니다.");
        }
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        if (orderLineItems == null) {
            throw new NullPointerException("주문 메뉴는 null일 수 없습니다.");
        }
    }

    public static Order create(
            OrderTable orderTable,
            OrderLineItems orderLineItems,
            OrderValidator orderValidator
    ) {
        return new Order(OrderStatus.COOKING, orderTable, orderLineItems, orderValidator);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("Completion 상태일 경우, 주문 상태를 변경할 수 없습니다.");
        }

        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

}
