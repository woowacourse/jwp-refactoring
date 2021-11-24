package kitchenpos.application.dto.request;

public class TableNumberOfGuestsRequestDto {

    private Long orderTableId;
    private Long numberOfGuests;

    private TableNumberOfGuestsRequestDto() {
    }

    public TableNumberOfGuestsRequestDto(Long orderTableId, Long numberOfGuests) {
        this.orderTableId = orderTableId;
        this.numberOfGuests = numberOfGuests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }
}
