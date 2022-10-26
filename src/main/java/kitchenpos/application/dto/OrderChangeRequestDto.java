package kitchenpos.application.dto;

public class OrderChangeRequestDto {

    private String orderStatus;

    public OrderChangeRequestDto(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
