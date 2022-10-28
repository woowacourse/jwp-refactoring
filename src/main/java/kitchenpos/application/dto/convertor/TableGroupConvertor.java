package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.request.OrderTableChangeRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupConvertor {

    private TableGroupConvertor() {
    }

    public static TableGroup convertToTableGroup(final TableGroupRequest request) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(convertToOrderTables(request.getOrderTables()));
        return tableGroup;
    }

    public static List<OrderTable> convertToOrderTables(final List<OrderTableChangeRequest> requests) {
        return requests.stream()
            .map(TableGroupConvertor::convertToOrderTable)
            .collect(Collectors.toUnmodifiableList());
    }

    public static OrderTable convertToOrderTable(final OrderTableChangeRequest request) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(request.getId());
        orderTable.setNumberOfGuests(request.getNumberOfGuests());
        orderTable.setEmpty(request.isEmpty());
        return orderTable;
    }

    public static TableGroupResponse convertToOrderTableResponse(final TableGroup orderTable) {
        return new TableGroupResponse(
            orderTable.getId(), orderTable.getCreatedDate(), convertToOrderTableResponses(orderTable.getOrderTables())
        );
    }

    public static List<OrderTableResponse> convertToOrderTableResponses(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable -> new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty()))
            .collect(Collectors.toUnmodifiableList());
    }
}
