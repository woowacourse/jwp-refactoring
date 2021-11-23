package kitchenpos.application.dto.request;

public class OrderTableRequestDto {

    private Long numberOfGuests;
    private Boolean empty;

    public OrderTableRequestDto(Long numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
