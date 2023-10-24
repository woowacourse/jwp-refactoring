package kitchenpos.domain.order;

import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kitchenpos.domain.order.OrderStatus.*;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order of(Long orderTableId, String orderStatus, LocalDateTime localDateTime, List<OrderLineItem> orderLineItems, long menuCounts) {
        validateOrderLineItemEmpty(orderLineItems);
        validateOrderLineItemSize(orderLineItems, menuCounts);
        return new Order(null, orderTableId, orderStatus, localDateTime);
    }

    private static void validateOrderLineItemEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateOrderLineItemSize(List<OrderLineItem> orderLineItems, long menuCounts) {
        if (orderLineItems.size() != menuCounts) {
            throw new IllegalArgumentException();
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

    public void changeOrderStatus(String orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (isCompleted(orderStatus)) {
            throw new IllegalArgumentException();
        }
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
