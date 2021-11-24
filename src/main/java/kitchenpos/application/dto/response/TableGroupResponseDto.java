package kitchenpos.application.dto.response;

public class TableGroupResponseDto {

    private Long id;

    private TableGroupResponseDto() {
    }

    public TableGroupResponseDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
