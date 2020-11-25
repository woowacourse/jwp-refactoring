package kitchenpos.domain;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public boolean isComplete() {
        return OrderStatus.COMPLETION.name().equals(this.orderStatus);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION.name().equals(this.orderStatus)) {
            throw new IllegalArgumentException("이미 결제가 끝난 주문은 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus.name();
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
