package kitchenpos.application;

import kitchenpos.domain.table.OrderCompletionValidator;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.domain.table.UngroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderCompletionValidator orderCompletionValidator;
    private final UngroupValidator ungroupValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
                             OrderCompletionValidator orderCompletionValidator, UngroupValidator ungroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderCompletionValidator = orderCompletionValidator;
        this.ungroupValidator = ungroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        var orderTableIds = request.getOrderTableIds();
        var orderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 포함되어 있습니다. ids = " + orderTableIds);
        }

        return TableGroupResponse.from(
                tableGroupRepository.save(new TableGroup(orderTables, orderCompletionValidator))
        );
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        var tableGroup = tableGroupRepository.getById(tableGroupId);
        tableGroup.ungroup(ungroupValidator, orderCompletionValidator);
    }
}
