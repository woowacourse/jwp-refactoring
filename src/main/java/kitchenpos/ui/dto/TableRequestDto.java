package kitchenpos.ui.dto;

import kitchenpos.application.dto.CreateTableDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TableRequestDto {

    private Integer numberOfGuests;
    private Boolean empty;

    public CreateTableDto toCreateTableDto() {
        return new CreateTableDto(numberOfGuests, empty);
    }
}
