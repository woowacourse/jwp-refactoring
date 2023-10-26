package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.controller.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.domain.OrderCompletionValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.exception.NotExistOrderTable;
import kitchenpos.tablegroup.exception.NotExistTableGroupException;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderCompletionValidator orderCompletionValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    
    public TableGroupService(final OrderCompletionValidator orderCompletionValidator,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderCompletionValidator = orderCompletionValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }
    
    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<OrderTable> savedOrderTables = getOrderTables(request.getOrderTables());
        TableGroup tableGroup = new TableGroup(savedOrderTables);
        updateOrderTables(tableGroup, savedOrderTables);
        return tableGroupRepository.save(tableGroup);
    }
    
    private List<OrderTable> getOrderTables(final List<Long> OrderTableIds) {
        return OrderTableIds.stream()
                            .map(orderTableId -> orderTableRepository.findById(orderTableId)
                                                                     .orElseThrow(() -> new NotExistOrderTable("존재하지 않는 테이블입니다")))
                            .collect(Collectors.toList());
    }
    
    private void updateOrderTables(final TableGroup tableGroup, final List<OrderTable> savedOrderTables) {
        savedOrderTables.forEach(orderTable -> orderTable.setTableGroup(tableGroup));
    }
    
    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableGroupRepository.findById(tableGroupId)
                            .orElseThrow(() -> new NotExistTableGroupException("존재하지 않는 단체 테이블입니다"));
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        orderCompletionValidator.validateIfOrderOfOrderTableIsCompleted(orderTables, tableGroupId);
        for (final OrderTable orderTable : orderTables) {
            orderTable.detachFromTableGroup();
        }
    }
}
