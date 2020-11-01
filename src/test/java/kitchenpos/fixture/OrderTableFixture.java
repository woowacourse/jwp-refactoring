package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static final OrderTable CREATE_ORDER_TABLE =
            TestObjectUtils.createOrderTable(1L, null, 3, false);

    public static final OrderTable ORDER_TABLE1 =
            TestObjectUtils.createOrderTable(1L, 1L, 3, false);

    public static final OrderTable ORDER_TABLE2 =
            TestObjectUtils.createOrderTable(2L, 1L, 3, false);

    public static final OrderTable ORDER_TABLE3 =
            TestObjectUtils.createOrderTable(3L, null, 3, false);

    public static final OrderTable CHANGING_ORDER_TABLE =
            TestObjectUtils.createOrderTable(3L, null, 5, true);

    public static final OrderTable INVALID_NUMBER_OF_GUEST_ORDER_TABLE =
            TestObjectUtils.createOrderTable(3L, null, -1, true);

    public static final OrderTable EMPTY_ORDER_TABLE =
            TestObjectUtils.createOrderTable(3L, null, -1, true);

    public static final List<OrderTable> ORDER_TABLES = Arrays.asList(ORDER_TABLE1, ORDER_TABLE2);

}
