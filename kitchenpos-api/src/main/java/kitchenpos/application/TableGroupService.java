package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.TableGroupCreateDto;
import kitchenpos.application.exception.TableGroupAppException;
import kitchenpos.application.exception.TableGroupAppException.EmptyOrderTablesCreateTableGroupException;
import kitchenpos.application.exception.TableGroupAppException.OrderTableCountMismatchException;
import kitchenpos.table.OrderStatusChecker;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.TableGroup;
import kitchenpos.table.TableGroupRepository;
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
        if (tableGroupCreateDto.getOrderTableIds().isEmpty()) {
            throw new EmptyOrderTablesCreateTableGroupException();
        }
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdInAndTableGroupIsNull(
            tableGroupCreateDto.getOrderTableIds());

        if (tableGroupCreateDto.getOrderTableIds().size() != savedOrderTables.size()) {
            throw new OrderTableCountMismatchException();
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
