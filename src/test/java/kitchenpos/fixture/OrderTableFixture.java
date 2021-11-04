package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTableFixture {
    public static final OrderTable FIRST;
    public static final OrderTable FIRST_EMPTY_FALSE;
    public static final OrderTable FIRST_EMPTY_FALSE_HAVE_GUEST;
    public static final OrderTable SECOND;
    public static final OrderTable SECOND_EMPTY_FALSE;
    public static final OrderTable THIRD;
    public static final OrderTable FOURTH;
    public static final OrderTable FIFTH;
    public static final OrderTable SIXTH;
    public static final OrderTable SEVENTH;
    public static final OrderTable EIGHT;
    public static final OrderTable NINTH;
    public static final OrderTable TO_EMPTY_TRUE;
    public static final OrderTable TO_EMPTY_FALSE;
    public static final OrderTable TO_NUMBER_OF_GUESTS_FIVE;
    public static final OrderTable TABLE_BEFORE_SAVE;
    public static final OrderTable TABLE_BEFORE_SAVE_EMPTY_FALSE;


    static {
        FIRST = newInstance(1L, true);
        FIRST_EMPTY_FALSE = newInstance(1L, false);
        FIRST_EMPTY_FALSE_HAVE_GUEST = newInstance(1L, false, 4);
        SECOND = newInstance(2L, true);
        SECOND_EMPTY_FALSE = newInstance(2L, false);
        THIRD = newInstance(3L, true);
        FOURTH = newInstance(4L, true);
        FIFTH = newInstance(5L, true);
        SIXTH = newInstance(6L, true);
        SEVENTH = newInstance(7L, true);
        EIGHT = newInstance(8L, true);
        NINTH = newInstance(9L, true);
        TO_EMPTY_TRUE = newInstance(null, true);
        TO_EMPTY_FALSE = newInstance(null, false);
        TO_NUMBER_OF_GUESTS_FIVE = newInstance(null, false, 5);
        TABLE_BEFORE_SAVE = newInstance(null, true);
        TABLE_BEFORE_SAVE_EMPTY_FALSE = newInstance(null, false);
    }

    public static List<OrderTable> orderTables() {
        return Arrays.asList(FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH, EIGHT, NINTH);
    }

    public static List<Long> orderTablesId() {
        return orderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private static OrderTable newInstance(Long id, boolean empty) {
        return newInstance(id, empty, 0);
    }

    private static OrderTable newInstance(Long id, boolean empty, int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
