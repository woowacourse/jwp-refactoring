package kitchenpos.application.dto.response;

public class TableNumberOfGuestsResponseDto {

    private Long numberOfGuests;

    private TableNumberOfGuestsResponseDto() {
    }

    public TableNumberOfGuestsResponseDto(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }
}
