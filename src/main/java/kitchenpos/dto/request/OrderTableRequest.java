package kitchenpos.dto.request;

public class OrderTableRequest {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(Long id) {
        this(id, null, 0, false);
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
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
