package kitchenpos.order.dto.request;

public class ChangeOrderStatusRequest {

    private String status;

    private ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
