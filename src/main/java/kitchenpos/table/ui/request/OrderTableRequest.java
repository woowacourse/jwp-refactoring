package kitchenpos.table.ui.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {

    @Min(value = 0, message = "손님 수는 음수일 수 없습니다.")
    private int numberOfGuests;

    @NotNull(message = "테이블 상태는 null일 수 없습니다.")
    private boolean empty;

    protected OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(
            numberOfGuests,
            empty
        );
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
