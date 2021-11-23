package kitchenpos.dto.response;

import kitchenpos.domain.OrderTable;

public class TableInGroupResponse {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    public TableInGroupResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableInGroupResponse from(OrderTable orderTable) {
        return new TableInGroupResponse(
                orderTable.getId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
