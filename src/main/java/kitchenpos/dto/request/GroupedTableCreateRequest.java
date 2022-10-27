package kitchenpos.dto.request;

public class GroupedTableCreateRequest {

    private Long id;

    private GroupedTableCreateRequest() {}

    public GroupedTableCreateRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
