package kitchenpos.dto.request.table;

public class TableIdRequest {
    private Long id;

    public TableIdRequest() {
    }

    public TableIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
