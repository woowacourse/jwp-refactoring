package kitchenpos.dto.request;

public class OrderTableRequest {

    private Long id;
    private Long tableGroupId;

    private OrderTableRequest() {
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
