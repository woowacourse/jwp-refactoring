package kitchenpos.application.dto.request;

public class UpdateGuestNumberDto {

    private final Long orderTableId;
    private final Integer numberOfGuests;

    public UpdateGuestNumberDto(Long orderTableId, Integer numberOfGuests) {
        this.orderTableId = orderTableId;
        this.numberOfGuests = numberOfGuests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
