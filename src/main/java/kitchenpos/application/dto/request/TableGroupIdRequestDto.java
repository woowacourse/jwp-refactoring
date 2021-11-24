package kitchenpos.application.dto.request;

public class TableGroupIdRequestDto {

    private Long id;

    private TableGroupIdRequestDto() {
    }

    public TableGroupIdRequestDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
