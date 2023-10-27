package kitchenpos.table.ui;

public class OrderTableDto {

    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableDto(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableDto(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
