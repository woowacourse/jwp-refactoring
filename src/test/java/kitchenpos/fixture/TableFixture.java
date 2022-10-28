package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableFixture {

    public static OrderTable getOrderTable() {
        return getOrderTable(1L, null, 2, false);
    }

    public static OrderTable getOrderTable(final Long tableGroupId) {
        return getOrderTable(1L, tableGroupId, 2, false);
    }

    public static OrderTable getOrderTable(final Long tableGroupId, final boolean empty) {
        return getOrderTable(1L, tableGroupId, 2, empty);
    }

    public static OrderTable getOrderTable(final int numberOfGuests) {
        return getOrderTable(1L, null, numberOfGuests, false);
    }

    public static OrderTable getOrderTable(final boolean empty) {
        return getOrderTable(1L, null, 1, empty);
    }

    public static OrderTable getOrderTable(final Long id,
                                           final Long tableGroupId,
                                           final int numberOfGuests,
                                           final boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public static TableGroup getTableGroupRequest() {
        return getTableGroupRequest(1L, LocalDateTime.now(), new LinkedList<>());
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
        return new TableGroup(id, createdDate, orderTables);
    }

    public static OrderTable getOrderTableRequest() {
        return getOrderTableRequest(1L, null, 2, true);
    }

    public static OrderTable getOrderTableRequest(final Long id,
                                                  final Long tableGroupId,
                                                  final int numberOfGuests,
                                                  final boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
