package kitchenpos.dto.request.order;

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
