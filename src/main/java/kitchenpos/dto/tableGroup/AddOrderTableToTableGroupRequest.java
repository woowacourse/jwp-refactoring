package kitchenpos.dto.tableGroup;

public class AddOrderTableToTableGroupRequest {

    private Long id;

    public AddOrderTableToTableGroupRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
