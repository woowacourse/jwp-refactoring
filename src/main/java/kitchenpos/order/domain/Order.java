package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime);
    }

    public Order(Long orderTableId) {
        this(null, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now());
    }

    public void changeOrderStatus(String orderStatusName) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException("완료된 주문은 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatusName;
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
}
