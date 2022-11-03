package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class Order {
    private final Long id;
    private final Long orderTableId;
    private final LocalDateTime orderedTime;
    private OrderStatus orderStatus;
    private List<OrderLineItem> orderLineItems;

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, null);
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                  List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        validateEmptyOrderLineItems(orderLineItems);
        return new Order(null, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    private static void validateEmptyOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 정보가 없습니다.");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isNotChangeStatus()) {
            throw new IllegalArgumentException("주문이 완료된 상태이므로 상태를 변화시킬 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    private boolean isNotChangeStatus() {
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

    public void changeOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
