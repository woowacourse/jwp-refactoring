package kitchenpos.order.ui.request;

public class OrderChangeRequest {

    private String status;

    public OrderChangeRequest() {
    }

    public OrderChangeRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
