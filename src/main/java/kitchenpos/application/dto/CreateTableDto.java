package kitchenpos.application.dto;

import lombok.Getter;

@Getter
public class CreateTableDto {

    private final Integer numberOfGuests;
    private final Boolean empty;

    public CreateTableDto(Integer numberOfGuests, Boolean empty) {
        this.empty = empty;
        this.numberOfGuests = numberOfGuests;
    }
}
