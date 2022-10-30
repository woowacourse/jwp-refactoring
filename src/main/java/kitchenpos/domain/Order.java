package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class Order {

    private final Long id;
    private final Long orderTableId;
    private String orderStatus;
    private final LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    private Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        validateEmptyOrderLineItem(orderLineItems);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validateEmptyOrderLineItem(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비었습니다.");
        }
    }

    public static Order createWithOutOrderLineItem(final long id, final long orderTableId, final String orderStatus,
                                                   final LocalDateTime orderedTime) {
        return new Order(id, orderTableId, orderStatus, orderedTime);
    }

    public boolean isValidMenuSize(final int menuCount) {
        return orderLineItems.size() == menuCount;
    }

    public void changeOrderStatus(final String changeOrderStatus) {
        validateCompletionOrderStatus();
        this.orderStatus = changeOrderStatus;
    }

    private void validateCompletionOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
            throw new IllegalArgumentException("완료된 주문 상태는 변경할 수 없습니다.");
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
