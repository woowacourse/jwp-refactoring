package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

public class Order {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;

    private Order() {
    }

    public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime);
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    @JsonIgnore
    public boolean isNotCompleted() {
        return OrderStatus.of(orderStatus) != COMPLETION;
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

    public void changeOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
