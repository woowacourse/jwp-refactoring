package kitchenpos.ordertable.application.dto;

public class CreateOrderTableDto {

    private Integer numberOfGuests;
    private Boolean empty;

    public CreateOrderTableDto() {
    }

    public CreateOrderTableDto(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
