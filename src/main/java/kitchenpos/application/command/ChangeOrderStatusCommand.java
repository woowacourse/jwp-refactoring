package kitchenpos.application.command;

import java.util.Arrays;

import kitchenpos.domain.model.order.OrderStatus;

public class ChangeOrderStatusCommand {
    private String orderStatus;

    private ChangeOrderStatusCommand() {
    }

    public ChangeOrderStatusCommand(String orderStatus) {
        this.orderStatus = orderStatus;
        validate(this.orderStatus);
    }

    private void validate(String orderStatus) {
        Arrays.stream(OrderStatus.values())
                .filter(value -> value.name().equals(orderStatus))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
