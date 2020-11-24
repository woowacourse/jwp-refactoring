package kitchenpos.domain;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isCompleted()) {
            throw new IllegalArgumentException("완료된 주문의 상태는 변경할 수 없습니다.");
        }

        this.orderStatus = orderStatus;
    }

    private boolean isCompleted() {
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
