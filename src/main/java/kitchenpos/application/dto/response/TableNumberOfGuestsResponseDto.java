package kitchenpos.application.dto.response;

public class TableNumberOfGuestsResponseDto {

    private Long numberOfGuests;

    public TableNumberOfGuestsResponseDto(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }
}
