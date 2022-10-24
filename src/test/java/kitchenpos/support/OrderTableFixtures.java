package kitchenpos.support;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;

public enum OrderTableFixtures {

    ORDER_TABLE1(1L, 0, true),
    ORDER_TABLE2(2L, 0, true),
    ORDER_TABLE3(3L, 0, true),
    ORDER_TABLE4(4L, 0, true),
    ORDER_TABLE5(5L, 0, true),
    ORDER_TABLE6(6L, 0, true),
    ORDER_TABLE7(7L, 0, true),
    ORDER_TABLE8(8L, 0, true),
    ;

    private Long id;
    private int numberOfGuests;
    private boolean empty;

    OrderTableFixtures(final Long id, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable create() {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public OrderTable createWithIdNull() {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public static List<OrderTable> createAll() {
        return Arrays.asList(ORDER_TABLE1.create(), ORDER_TABLE2.create(), ORDER_TABLE3.create(), ORDER_TABLE4.create(),
                ORDER_TABLE5.create(), ORDER_TABLE6.create(), ORDER_TABLE7.create(), ORDER_TABLE8.create());
    }
}
