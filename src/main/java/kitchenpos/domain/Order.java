package kitchenpos.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Order {

    private final Long id;
    private final Long orderTableId;
    private OrderStatus orderStatus;
    private final LocalDateTime orderedTime;

    public Order(Long id,
                 Long orderTableId,
                 OrderStatus orderStatus,
                 LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order of(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return new Order(null, orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now());
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }
}
