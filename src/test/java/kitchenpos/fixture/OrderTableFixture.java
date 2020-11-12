package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static final OrderTable ORDER_TABLE1 =
            TestObjectUtils.createOrderTable(1L, null, 0, true);

    public static final OrderTable ORDER_TABLE2 =
            TestObjectUtils.createOrderTable(2L, null, 0, true);

    public static final OrderTable ORDER_TABLE3 =
            TestObjectUtils.createOrderTable(3L, 1L, 3, false);

    public static final OrderTable ORDER_TABLE4 =
            TestObjectUtils.createOrderTable(4L, 1L, 3, false);

    public static final OrderTable ORDER_TABLE5 =
            TestObjectUtils.createOrderTable(1L, null, 3, false);

    public static final OrderTable CHANGING_NOT_EMPTY_ORDER_TABLE =
            TestObjectUtils.createOrderTable(null, null, 5, false);

    public static final OrderTable CHANGING_GUEST_ORDER_TABLE =
            TestObjectUtils.createOrderTable(null, null, 5, false);

    public static final OrderTable INVALID_NUMBER_OF_GUEST_ORDER_TABLE =
            TestObjectUtils.createOrderTable(null, null, -1, true);

    public static final OrderTable EMPTY_ORDER_TABLE =
            TestObjectUtils.createOrderTable(null, null, 8, true);

    public static final List<OrderTable>
            ORDER_TABLES1 = Arrays.asList(ORDER_TABLE1, ORDER_TABLE2);

    public static final List<OrderTable>
            ORDER_TABLES2 = Arrays.asList(ORDER_TABLE3, ORDER_TABLE4);
}
