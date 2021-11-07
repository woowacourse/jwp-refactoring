package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.OrdersRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import kitchenpos.ui.dto.TableRequest;
import kitchenpos.ui.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrdersRepository ordersRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
                             OrdersRepository ordersRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.ordersRepository = ordersRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = mapToOrderTables(tableGroupRequest);
        orderTables.forEach(OrderTable::validate);

        TableGroup newTableGroup = tableGroupRepository.save(new TableGroup());
        List<TableResponse> orderTableResponses = createOrderTableResponses(orderTables, newTableGroup);

        return TableGroupResponse.of(newTableGroup, orderTableResponses);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        orderTables.forEach(this::validateToUnGroup);
        orderTables.forEach(OrderTable::ungroup);
    }

    private List<OrderTable> mapToOrderTables(TableGroupRequest tableGroupRequest) {
        validateTableSize(tableGroupRequest.getTableRequests());
        List<Long> tableIds = tableGroupRequest.getTableRequests().stream()
                .map(TableRequest::getId)
                .collect(Collectors.toList());
        return orderTableRepository.findAllById(tableIds);
    }

    private List<TableResponse> createOrderTableResponses(List<OrderTable> orderTables, TableGroup newTableGroup) {
        return orderTables.stream()
                .map(orderTable -> {
                    orderTable.assignTableGroup(newTableGroup);
                    orderTable.changeEmpty(false);
                    return orderTableRepository.save(orderTable);
                })
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    private void validateToUnGroup(OrderTable orderTable) {
        List<Orders> allFoundOrders = ordersRepository.findAllByOrderTableId(orderTable.getId());
        allFoundOrders.forEach(Orders::validateCompleted);
    }

    private void validateTableSize(List<TableRequest> tableRequests) {
        if (CollectionUtils.isEmpty(tableRequests) || tableRequests.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 대상 테이블은 두 개 이상이어야 합니다.");
        }
    }
}
