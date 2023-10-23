package kitchenpos.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TableRequest {

    @NotNull(message = "인원 수를 입력해 주세요.")
    @Min(0)
    private final int numberOfGuests;

    @NotNull(message = "테이블의 빈 상태 정보를 입력해 주세요.")
    private final boolean empty;

    public TableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
