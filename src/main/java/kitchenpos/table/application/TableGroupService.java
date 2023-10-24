package kitchenpos.table.application;

import kitchenpos.table.application.dto.request.CreateTableGroupRequest;
import kitchenpos.table.application.dto.request.CreateTableGroupRequest.TableInfo;
import kitchenpos.table.application.dto.response.CreateTableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public CreateTableGroupResponse create(CreateTableGroupRequest createTableGroupRequest) {
        List<OrderTable> orderTables = findOrderTables(createTableGroupRequest.getOrderTables());
        validateOrderTables(createTableGroupRequest, orderTables);

        TableGroup tableGroup = new TableGroup();
        tableGroup.addOrderTables(orderTables);

        tableGroupRepository.save(tableGroup);
        return CreateTableGroupResponse.from(tableGroup);
    }

    private void validateOrderTables(CreateTableGroupRequest createTableGroupRequest, List<OrderTable> orderTables) {
        if (orderTables.size() != createTableGroupRequest.getOrderTables().size()) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.existsTableGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }

    private List<OrderTable> findOrderTables(List<TableInfo> tableGroupOrderTablesRequest) {
        List<Long> orderTableIds = tableGroupOrderTablesRequest.stream()
                .map(TableInfo::getId)
                .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateOrder(orderTables);

        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateOrder(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        tableValidator.validateIsTableGroupCompleteMeal(orderTableIds);
    }
}
