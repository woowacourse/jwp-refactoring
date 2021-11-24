package kitchenpos.ui.dto.response;

public class TableResponse {

    private Long id;

    private TableResponse() {
    }

    public TableResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
