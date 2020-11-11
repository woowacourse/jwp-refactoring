package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableDto;

public class OrderTableFixture {
    public static final Long ID1 = 1L;
    public static final Long ID2 = 2L;
    public static final Long ID3 = 3L;
    public static final Long ID4 = 4L;
    public static final int DEFAULT_NUMBER_OF_GUESTS = 0;
    public static final boolean DEFAULT_EMPTY = true;

    public static OrderTable createEmptyWithId(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        return orderTable;
    }

    public static OrderTable createEmptyWithoutId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        return orderTable;
    }

    public static OrderTable createNotEmptyWithId(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(false);

        return orderTable;
    }

    public static OrderTable createNumOf(Long id, int numOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(numOfGuests);
        orderTable.setEmpty(false);

        return orderTable;
    }

    public static OrderTable createGroupTableWithId(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(false);

        return orderTable;
    }

    public static OrderTableDto createDto() {
        return new OrderTableDto(DEFAULT_NUMBER_OF_GUESTS, DEFAULT_EMPTY);
    }
}
