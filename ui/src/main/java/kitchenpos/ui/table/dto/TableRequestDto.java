package kitchenpos.ui.table.dto;

import kitchenpos.application.table.dto.request.CreateTableDto;

public class TableRequestDto {

    private Integer numberOfGuests;
    private Boolean empty;

    public TableRequestDto() {
    }

    public CreateTableDto toCreateTableDto() {
        return new CreateTableDto(numberOfGuests, empty);
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
