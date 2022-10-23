package kitchenpos.application.dto;

public class CreateTableDto {

    private final Integer numberOfGuests;
    private final Boolean empty;

    public CreateTableDto(Integer numberOfGuests, Boolean empty) {
        this.empty = empty;
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
