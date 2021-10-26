package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

import java.util.Arrays;
import java.util.List;

public class OrderTableFixture {
    public static OrderTable FIRST;
    public static OrderTable FIRST_EMPTY_FALSE;
    public static OrderTable FIRST_EMPTY_FALSE_HAVE_GUEST;
    public static OrderTable SECOND;
    public static OrderTable THIRD;
    public static OrderTable FOURTH;
    public static OrderTable FIFTH;
    public static OrderTable SIXTH;
    public static OrderTable SEVENTH;
    public static OrderTable EIGHT;
    public static OrderTable NINTH;

    static {
        FIRST = newInstance(1L, true);
        FIRST_EMPTY_FALSE = newInstance(1L, false);
        FIRST_EMPTY_FALSE_HAVE_GUEST = newInstance(1L, false, 4);
        SECOND = newInstance(2L, true);
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
