package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.CreateTableDto;

public class TableRequestDto {

    private Integer numberOfGuests;
    private Boolean empty;

    public TableRequestDto() {
    }

    public CreateTableDto toCreateTableDto() {
        return new CreateTableDto(numberOfGuests, empty);
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }
}
