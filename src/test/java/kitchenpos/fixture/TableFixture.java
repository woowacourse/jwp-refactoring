package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;

public class TableFixture {

    public static OrderTable getOrderTable() {
        return getOrderTable(1L, null, 2, false, null);
    }

    public static OrderTable getOrderTable(final Long tableGroupId) {
        return getOrderTable(1L, tableGroupId, 2, false, null);
    }

    public static OrderTable getOrderTable(final Long tableGroupId, final boolean empty) {
        return getOrderTable(1L, tableGroupId, 2, empty, null);
    }

    public static OrderTable getOrderTable(final Long tableGroupId, final boolean empty, final String orderStatus) {
        return getOrderTable(1L, tableGroupId, 2, empty, orderStatus);
    }

    public static OrderTable getOrderTable(final int numberOfGuests) {
        return getOrderTable(1L, null, numberOfGuests, false, null);
    }

    public static OrderTable getOrderTable(final boolean empty) {
        return getOrderTable(1L, null, 1, empty, null);
    }

    public static OrderTable getOrderTable(final Long id,
                                           final Long tableGroupId,
                                           final int numberOfGuests,
                                           final boolean empty,
                                           final String orderStatus) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty, OrderStatus.from(orderStatus));
    }

    public static TableGroup getTableGroupRequest() {
        return getTableGroupRequest(1L, LocalDateTime.now(), Arrays.asList(getOrderTableRequest(), getOrderTableRequest()));
    }

    public static TableGroup getTableGroupRequest(final List<OrderTable> orderTables) {
        return getTableGroupRequest(1L, LocalDateTime.now(), orderTables);
    }

    public static TableGroup getTableGroupRequest(final LocalDateTime createDate) {
        return getTableGroupRequest(1L, createDate, new LinkedList<>());
    }

    public static TableGroup getTableGroupRequest(final Long id,
                                                  final LocalDateTime createdDate,
                                                  final List<OrderTable> orderTables) {
        return TableGroup.of(id, createdDate, orderTables);
    }

    public static OrderTable getOrderTableRequest() {
        return getOrderTableRequest(null, 2, true);
    }

    public static OrderTable getOrderTableRequest(final Long tableGroupId,
                                                  final int numberOfGuests,
                                                  final boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }
}
