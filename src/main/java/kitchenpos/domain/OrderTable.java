package kitchenpos.domain;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id) {
        this(id, null, 0, true);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
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

    // TODO add 로직 다 없애기
    public void addEmpty(boolean empty) {
        this.empty = empty;
    }

    public void addNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void addTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }
}
