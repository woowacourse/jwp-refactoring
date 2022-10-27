package kitchenpos.dto;

public class OrderStatusChangeReqeust {

    private String status;

    public OrderStatusChangeReqeust(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
