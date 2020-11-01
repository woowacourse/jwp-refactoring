package kitchenpos.ui.dto;

public class TableOfTableGroupRequest {

    private Long id;

    private TableOfTableGroupRequest() {
    }

    public TableOfTableGroupRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TableOfTableGroupRequest{" +
            "id=" + id +
            '}';
    }
}
