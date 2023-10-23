package kitchenpos.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TableGuestUpdateRequest {

    @NotNull(message = "인원 수를 입력해 주세요")
    @Min(0)
    private final int numberOfGuests;

    public TableGuestUpdateRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
