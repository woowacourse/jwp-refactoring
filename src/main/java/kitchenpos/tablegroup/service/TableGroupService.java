package kitchenpos.tablegroup.service;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.OrderTableNotFoundException;
import kitchenpos.ordertable.repository.TableRepository;
import kitchenpos.ordertable.validator.OrderChecker;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final OrderChecker orderChecker;

    public TableGroupService(TableRepository tableRepository, TableGroupRepository tableGroupRepository,
                             TableGroupValidator tableGroupValidator,
                             OrderChecker orderChecker) {
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.orderChecker = orderChecker;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> orderTableIds = tableGroupCreateRequest.getOrderTables();
        List<OrderTable> savedOrderOrderTables = tableRepository.findAllByIdIn(orderTableIds);
        validateNotFoundOrderTable(savedOrderOrderTables.size(), orderTableIds.size());
        OrderTables orderTables = OrderTables.forGrouping(savedOrderOrderTables);

        TableGroup tableGroup = new TableGroup();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        orderTables.group(savedTableGroup.getId());
        return new TableGroupResponse(savedTableGroup, orderTables.getValues());
    }

    private void validateNotFoundOrderTable(int requestedOrderTableSize, int savedOrderTableSize) {
        if (requestedOrderTableSize != savedOrderTableSize) {
            throw new OrderTableNotFoundException();
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTablesInTableGroup = tableRepository.findAllByTableGroupId(tableGroupId);
        OrderTables orderTables = OrderTables.forUnGrouping(orderTablesInTableGroup);
        orderTables.ungroup(tableGroupValidator);
        orderTables.setEmpty(orderChecker);
    }
}
