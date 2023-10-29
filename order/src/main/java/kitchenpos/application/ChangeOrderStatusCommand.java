package kitchenpos.application;

public class ChangeOrderStatusCommand {

    private Long orderId;
    private String orderStatus;

    public ChangeOrderStatusCommand(final Long orderId, final String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

}
