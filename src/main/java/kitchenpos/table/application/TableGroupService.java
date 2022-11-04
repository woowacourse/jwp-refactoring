package kitchenpos.table.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.validator.TableGroupValidator;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.dto.TableGroupSaveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(final TableGroupValidator tableGroupValidator,
                             final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupSaveRequest request) {
        List<Long> orderTableIds = toOrderTableIds(request.getOrderTables());
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateSizeOrderTable(orderTableIds, savedOrderTables);
        validateEmptyOrderTables(savedOrderTables);

        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(request.getOrderTables()));
        return new TableGroupResponse(savedTableGroup);
    }

    private List<Long> toOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());
    }

    private void validateSizeOrderTable(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmptyOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable savedOrderTable : orderTables) {
            validateEmptyOrderTable(savedOrderTable);
        }
    }

    private void validateEmptyOrderTable(final OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup savedTableGroup = getById(tableGroupId);
        savedTableGroup.ungroup(tableGroupValidator.validate(savedTableGroup));
    }

    private TableGroup getById(final Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
