package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.ordertable.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    public TableGroup create(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || savedOrderTable.isBelongToTableGroup()) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.enrollTableGroupId(savedTableGroup.getId());
            savedOrderTable.changeEmptyStatus(false);
        }

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        tableValidator.validateUnGroupable(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.releaseTableGroupId();
            orderTable.changeEmptyStatus(false);
        }
    }
}
