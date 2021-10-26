package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

import java.util.Arrays;
import java.util.List;

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
    }

    public static List<OrderTable> orderTables() {
        return Arrays.asList(FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH, EIGHT, NINTH);
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
