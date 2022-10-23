package kitchenpos.ui.dto;

import kitchenpos.application.dto.CreateTableDto;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class TableRequestDto {

    private Integer numberOfGuests;
    private Boolean empty;

    public CreateTableDto toCreateTableDto() {
        return new CreateTableDto(numberOfGuests, empty);
    }
}
