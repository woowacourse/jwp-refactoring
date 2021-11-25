package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupValidator;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupValidator tableGroupValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final TableGroupValidator tableGroupValidator,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.tableGroupValidator = tableGroupValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(tableGroup.getOrderTables().getOrderTableIds()));
        tableGroup.grouping(tableGroupValidator, savedOrderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        savedOrderTables.groupingTables(savedTableGroup.getId());

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        tableGroup.unGroup(tableGroupValidator);
        tableGroupRepository.delete(tableGroup);
    }
}
