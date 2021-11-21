package kitchenpos.ui.dto.request.table;

public class OrderTableRequestDto {

    private int numberOfGuests;
    private Boolean empty;

    private OrderTableRequestDto() {
    }

    public OrderTableRequestDto(int numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
