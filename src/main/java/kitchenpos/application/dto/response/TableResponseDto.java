package kitchenpos.application.dto.response;

public class TableResponseDto {

    private Long id;

    private TableResponseDto() {
    }

    public TableResponseDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
