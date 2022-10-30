package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.request.OrderTableChangeRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class TableGroupConvertor {

    private TableGroupConvertor() {
    }

    public static TableGroup toTableGroup(final TableGroupRequest request) {
        return new TableGroup(toOrderTables(request.getOrderTables()));
    }

    public static TableGroupResponse toOrderTableResponse(final TableGroup orderTable) {
        return new TableGroupResponse(
            orderTable.getId(), orderTable.getCreatedDate(), toOrderTableResponses(orderTable.getOrderTables())
        );
    }

    private static List<OrderTable> toOrderTables(final List<OrderTableChangeRequest> requests) {
        return requests.stream()
            .map(TableGroupConvertor::toOrderTable)
            .collect(Collectors.toUnmodifiableList());
    }

    private static OrderTable toOrderTable(final OrderTableChangeRequest request) {
        return new OrderTable(
            request.getId(),
            request.getNumberOfGuests(),
            request.isEmpty()
        );
    }

    private static List<OrderTableResponse> toOrderTableResponses(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable -> new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty()))
            .collect(Collectors.toUnmodifiableList());
    }
}
