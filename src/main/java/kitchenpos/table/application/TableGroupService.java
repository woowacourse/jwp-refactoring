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
    private final TableOrderStatusValidator tableOrderStatusValidator;
    private final TablesValidator tablesValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, TableOrderStatusValidator tableOrderStatusValidator, TablesValidator tablesValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableOrderStatusValidator = tableOrderStatusValidator;
        this.tablesValidator = tablesValidator;
    }

    @Transactional
    public CreateTableGroupResponse create(CreateTableGroupRequest createTableGroupRequest) {
        List<OrderTable> orderTables = findOrderTables(createTableGroupRequest.getOrderTables());
        tablesValidator.validate(orderTables, createTableGroupRequest);

        TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);
        saveOrderTables(orderTables, tableGroup);
        return CreateTableGroupResponse.of(tableGroup, orderTables);
    }

    private List<OrderTable> findOrderTables(List<TableInfo> tableGroupOrderTablesRequest) {
        List<Long> orderTableIds = tableGroupOrderTablesRequest.stream()
                .map(TableInfo::getId)
                .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private void saveOrderTables(List<OrderTable> orderTables, TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(tableGroup);
            orderTableRepository.save(orderTable);
        }
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
        tableOrderStatusValidator.validateIsTableGroupCompleteMeal(orderTableIds);
    }
}
