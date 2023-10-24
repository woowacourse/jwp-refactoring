package kitchenpos.dto;

public class OrderChangeDto {

    private final String orderStatus;

    public OrderChangeDto(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
