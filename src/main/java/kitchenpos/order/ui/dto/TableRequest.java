package kitchenpos.order.ui.dto;

import kitchenpos.order.domain.OrderTable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TableRequest {
    @NotNull(message = "방문한 손님 수가 null입니다.")
    @Min(value = 0, message = "방문한 손님 수가 음수입니다.")
    private int numberOfGuests;

    @NotNull(message = "빈 테이블 유무가 null입니다.")
    private boolean empty;

    private TableRequest() {
    }

    private TableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableRequest of(int numberOfGuests, boolean empty) {
        return new TableRequest(numberOfGuests, empty);
    }

    public static TableRequest empty(boolean empty) {
        return new TableRequest(0, empty);
    }

    public static TableRequest guests(int numberOfGuests) {
        return new TableRequest(numberOfGuests, false);
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
