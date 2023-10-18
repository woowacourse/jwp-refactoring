package kitchenpos.domain;

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
            OrderLineItems orderLineItems
    ) {
        this.orderStatus = orderStatus;
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(
            OrderStatus orderStatus,
            OrderTable orderTable,
            List<OrderLineItem> orderLineItems
    ) {
        validateOrderTable(orderTable);

        return new Order(orderStatus, orderTable, OrderLineItems.from(orderLineItems));
    }

    public static Order create(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrderTable(orderTable);
//        orderTable.changeEmpty(Boolean.TRUE);

        return new Order(OrderStatus.COOKING, orderTable, OrderLineItems.from(orderLineItems));
    }

    public static Order createWithEmptyOrderLinItems(OrderTable orderTable) {
        validateOrderTable(orderTable);

        return new Order(OrderStatus.COOKING, orderTable, OrderLineItems.createEmptyOrderLineItems());
    }


    private static void validateOrderTable(OrderTable orderTable) {
        if (orderTable == null) {
            throw new NullPointerException();
        }

        if (Boolean.TRUE.equals(orderTable.isEmpty())) {
            throw new IllegalArgumentException("주문할 수 없는 상태의 테이블이 존재합니다.");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("Completion 상태일 경우, 주문 상태를 변경할 수 없습니다.");
        }

        this.orderStatus = orderStatus;
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public void initializeOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = OrderLineItems.from(orderLineItems);
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
