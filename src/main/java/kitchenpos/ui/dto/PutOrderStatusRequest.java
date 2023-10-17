package kitchenpos.ui.dto;

public class PutOrderStatusRequest {

    private String orderStatus;

    public PutOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

}
