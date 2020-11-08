package kitchenpos.domain.entity;

import kitchenpos.domain.service.OrderTableChangeEmptyService;

public class OrderTable {
    private final Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty, OrderTableChangeEmptyService orderTableChangeEmptyService) {
        orderTableChangeEmptyService.validate(id, tableGroupId);
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void makeTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        empty = false;
    }

    public void resetTableGroup() {
        this.tableGroupId = null;
        empty = false;
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
