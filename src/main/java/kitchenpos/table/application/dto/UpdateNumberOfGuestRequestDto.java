package kitchenpos.table.application.dto;

public class UpdateNumberOfGuestRequestDto {

    private Integer numberOfGuests;

    public UpdateNumberOfGuestRequestDto(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
