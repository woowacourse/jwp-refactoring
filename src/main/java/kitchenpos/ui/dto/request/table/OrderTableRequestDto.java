package kitchenpos.ui.dto.request.table;

public class OrderTableRequestDto {

    private int numberOfGuests;
    private Boolean empty;

    private OrderTableRequestDto() {
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
