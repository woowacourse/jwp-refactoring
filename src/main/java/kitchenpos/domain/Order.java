package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
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
}
