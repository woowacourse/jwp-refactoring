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

    @NotNull
    private Long orderTableId;

    @Embedded
    private OrderLineItems orderLineItems;

    @CreatedDate
    @NotNull
    private LocalDateTime orderedTime;

    protected Order() {
    }

    private Order(
            OrderStatus orderStatus,
            Long orderTableId,
            OrderLineItems orderLineItems,
            OrderValidator orderValidator
    ) {
        validate(orderTableId, orderLineItems);
        orderValidator.validateOrderLineItems(orderLineItems);
        orderValidator.validateOrderTable(orderTableId);

        this.orderStatus = orderStatus;
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    private void validate(Long orderTableId, OrderLineItems orderLineItems) {
        validateOrderTable(orderTableId);
        validateOrderLineItems(orderLineItems);
    }

    private void validateOrderTable(Long orderTableId) {
        if (orderTableId == null) {
            throw new NullPointerException("주문 테이블은 null일 수 없습니다.");
        }
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        if (orderLineItems == null) {
            throw new NullPointerException("주문 메뉴는 null일 수 없습니다.");
        }
    }

    public static Order create(
            Long orderTableId,
            OrderLineItems orderLineItems,
            OrderValidator orderValidator
    ) {
        return new Order(OrderStatus.COOKING, orderTableId, orderLineItems, orderValidator);
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

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

}
