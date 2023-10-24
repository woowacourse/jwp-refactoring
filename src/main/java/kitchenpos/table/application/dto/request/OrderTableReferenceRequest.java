package kitchenpos.table.application.dto.request;

public class OrderTableReferenceRequest {

    private Long id;

    public OrderTableReferenceRequest(final Long id) {
        this.id = id;
    }

    public OrderTableReferenceRequest() {
    }
 
    public Long getId() {
        return id;
    }
}
