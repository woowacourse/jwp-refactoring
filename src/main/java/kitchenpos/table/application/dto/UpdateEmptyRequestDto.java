package kitchenpos.table.application.dto;

public class UpdateEmptyRequestDto {

    private Boolean empty;

    public UpdateEmptyRequestDto(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
