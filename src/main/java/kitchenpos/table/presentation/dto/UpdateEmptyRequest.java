package kitchenpos.table.presentation.dto;

import kitchenpos.table.application.dto.UpdateEmptyRequestDto;

public class UpdateEmptyRequest {

    private Boolean empty;

    public UpdateEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public UpdateEmptyRequestDto toServiceDto(){
        return new UpdateEmptyRequestDto(empty);
    }
}
