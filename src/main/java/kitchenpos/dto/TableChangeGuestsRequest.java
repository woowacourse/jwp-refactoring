package kitchenpos.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.Min;

public class TableChangeGuestsRequest {
    @Min(value = 0, message = "0명 미만이 될수 없습니다.")
    private final int numberOfGuests;

    @ConstructorProperties({"numberOfGuests"})
    public TableChangeGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
