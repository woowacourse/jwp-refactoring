package kitchenpos.application.dto;

public class OrderStatusDto {

    private String orderStatus;

    private OrderStatusDto() {
    }

    public OrderStatusDto(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
