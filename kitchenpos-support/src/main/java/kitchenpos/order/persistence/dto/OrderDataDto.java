package kitchenpos.order.persistence.dto;

import java.time.LocalDateTime;

public class OrderDataDto {

    private Long id;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private Long orderTableId;

    public OrderDataDto(final Long id,
                        final Long orderTableId,
                        final String orderStatus,
                        final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
