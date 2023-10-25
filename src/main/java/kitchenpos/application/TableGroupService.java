package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.TableGroupCreateDto;
import kitchenpos.application.exception.TableGroupAppException;
import kitchenpos.domain.table.OrderStatusChecker;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderStatusChecker orderStatusChecker;

    public TableGroupService(final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository,
        final OrderStatusChecker orderStatusChecker) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderStatusChecker = orderStatusChecker;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateDto tableGroupCreateDto) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(
            tableGroupCreateDto.getOrderTableIds());

        if (tableGroupCreateDto.getOrderTableIds().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        final TableGroup tableGroup = TableGroup.of(savedOrderTables);

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup findTableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(
                () -> new TableGroupAppException.NotFoundTableGroupException(tableGroupId));

        findTableGroup.ungroup(orderStatusChecker);

        tableGroupRepository.save(findTableGroup);
    }
}
