package kitchenpos.application.dto.request;

public class TableIdRequestDto {

    private Long id;

    private TableIdRequestDto() {
    }

    public TableIdRequestDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
