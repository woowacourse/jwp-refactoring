package kitchenpos.application.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableValidator;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.table.OrderTableRepository;
import kitchenpos.repository.table.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             OrderTableRepository orderTableRepository,
                             OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = mapToOrderTables(request.getOrderTableIds());
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables, LocalDateTime.now()));
        reflectChangeToOrderTables(orderTables, tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> mapToOrderTables(final List<Long> orderTableIds) {
        return orderTableIds.stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(final Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CustomError.TABLE_NOT_FOUND_ERROR));
    }

    private void reflectChangeToOrderTables(final List<OrderTable> orderTables, final TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroupId(tableGroup.getId());
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundException(CustomError.TABLE_GROUP_NOT_FOUND_ERROR));
        tableGroup.ungroup(orderTableValidator);
    }
}
