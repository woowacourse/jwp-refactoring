package kitchenpos.support.fixture.domain;

import kitchenpos.domain.OrderTable;

public enum OrderTableFixture {

    GUEST_ONE(1),
    GUEST_TWO(2),
    ;

    private final int numberOfGuests;

    OrderTableFixture(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable getOrderTable(Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public OrderTable getOrderTable(Long id, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
