package kitchenpos.order.ui.dto.request;

public class OrderStatusRequestDto {

    private String orderStatus;

    private OrderStatusRequestDto() {
    }

    public OrderStatusRequestDto(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
