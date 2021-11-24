package kitchenpos.application.dto.response;

public class TableEmptyResponseDto {

    private Boolean empty;

    public TableEmptyResponseDto(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
