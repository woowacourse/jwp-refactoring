package kitchenpos.domain;

public class OrderTable {

    private final Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    /**
     * DB 에 저장되지 않은 객체
     */
    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    /**
     * OrderTable 정보를 수정하기 위한 객체
     */
    public OrderTable(final Long id, final int numberOfGuests, final boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    /**
     * DB 에 저장된 객체
     */
    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuestsIsHigherZero(numberOfGuests);
        validateTableIsNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    public void group(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        empty = false;
    }

    private void validateNumberOfGuestsIsHigherZero(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(String.format("손님의 수는 0명 이하일 수 없습니다. [%s]", numberOfGuests));
        }
    }

    private void validateTableIsNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }
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

    public boolean isNotEmpty() {
        return !empty;
    }
}
