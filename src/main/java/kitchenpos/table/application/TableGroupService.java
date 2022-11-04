package kitchenpos.table.application;

import static kitchenpos.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.CustomIllegalArgumentException;
import kitchenpos.table.application.response.TableGroupResponse;
import kitchenpos.table.domain.JpaOrderTableRepository;
import kitchenpos.table.domain.JpaTableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.ui.request.OrderTableIdRequest;
import kitchenpos.table.ui.request.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final TableValidator validator;
    private final JpaOrderTableRepository orderTableRepository;
    private final JpaTableGroupRepository tableGroupRepository;

    public TableGroupService(final TableValidator validator, final JpaOrderTableRepository orderTableRepository,
                             final JpaTableGroupRepository tableGroupRepository) {
        this.validator = validator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        final TableGroup tableGroup = toTableGroup(request);
        final TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(saveTableGroup);
    }

    private TableGroup toTableGroup(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = getSavedOrderTables(request.getOrderTables());
        return TableGroup.of(LocalDateTime.now(), savedOrderTables);
    }

    private List<OrderTable> getSavedOrderTables(final List<OrderTableIdRequest> orderTables) {
        final List<OrderTable> savedOrderTables = getOrderTables(orderTables);
        validateSize(orderTables, savedOrderTables);
        return savedOrderTables;
    }

    private void validateSize(final List<OrderTableIdRequest> targetOrderTables,
                              final List<OrderTable> savedOrderTables) {
        if (targetOrderTables.size() != savedOrderTables.size()) {
            throw new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION);
        }
    }

    private List<OrderTable> getOrderTables(final List<OrderTableIdRequest> orderTables) {
        final List<Long> orderTableIds = extractOrderTableId(orderTables);
        return orderTableRepository.findAllById(orderTableIds);
    }

    private List<Long> extractOrderTableId(final List<OrderTableIdRequest> orderTables) {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getTableGroup(tableGroupId);
        validator.validateUngroup(tableGroup);
        tableGroup.ungroup();
    }

    private TableGroup getTableGroup(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION));
    }
}
