package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Objects;

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
        validate(orderTableId, orderStatus, orderedTime);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    private void validate(Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        validateOrderStatus(orderStatus);

        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException("어떤 테이블이 주문하고자 하는지 지정해주세요.");
        }
        if (Objects.isNull(orderedTime)) {
            throw new IllegalArgumentException("orderTime cannot be null.");
        }
    }

    private void validateOrderStatus(String orderStatus) {
        if (Objects.isNull(orderStatus) || orderStatus.isEmpty()) {
            throw new IllegalArgumentException("orderStatus of Order cannot be empty.");
        }
        if (!OrderStatus.isDefinedOrderStatus(orderStatus)) {
            throw new IllegalArgumentException(orderStatus + " is not a defined order status.");
        }
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
