package kitchenpos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.GuestNumberRequest;
import kitchenpos.table.application.dto.OrderTableRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableFixture {

    public static TableGroupRequest createTableGroupRequest(List<Long> ids) {
        final List<OrderTableRequest> orderTableRequests = ids.stream()
                .map(OrderTableRequest::new)
                .collect(Collectors.toList());
        return new TableGroupRequest(orderTableRequests);
    }

    public static TableGroup createTableGroup() {
        final Long tableGroupId = 1L;
        return TableGroup.builder()
                .id(tableGroupId)
                .createdDate(LocalDateTime.now())
                .build();
    }

    private static OrderTable createOrderTable(Long id, Long tableGroupId) {
        return OrderTable.builder()
                .id(id)
                .tableGroupId(tableGroupId)
                .numberOfGuests(1)
                .empty(false)
                .build();
    }

    public static OrderTable createOrderTable() {
        return OrderTable.builder()
                .id(1L)
                .tableGroupId(1L)
                .numberOfGuests(2)
                .empty(false)
                .build();
    }

    public static OrderTable updateOrderTableGuestNumber(OrderTable orderTable, GuestNumberRequest request) {
        return OrderTable.builder()
                .id(orderTable.getId())
                .tableGroupId(orderTable.getTableGroupId())
                .numberOfGuests(request.getNumberOfGuests())
                .empty(orderTable.isEmpty())
                .build();
    }

    public static OrderTableRequest createOrderTableRequest(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getId());
    }

    public static TableGroupResponse createTableGroupResponse() {
        return new TableGroupResponse(createTableGroup());
    }

}
