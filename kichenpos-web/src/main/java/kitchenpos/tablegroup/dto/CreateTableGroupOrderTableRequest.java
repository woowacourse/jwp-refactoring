package kitchenpos.tablegroup.dto;

public class CreateTableGroupOrderTableRequest {

    private Long id;

    public CreateTableGroupOrderTableRequest() {
    }

    public CreateTableGroupOrderTableDto toCreateTableGroupOrderTableDto() {
        return new CreateTableGroupOrderTableDto(id);
    }
}
