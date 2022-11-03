package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.ui.dto.TableGroupResponse;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.ui.dto.OrderTableRequestDto;
import kitchenpos.table.ui.dto.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupRepository tableGroupRepository;
    private final OrderValidator orderValidator;

    public TableGroupService(final OrderTableDao orderTableDao, final TableGroupRepository tableGroupRepository,
                             final OrderValidator orderValidator) {
        this.orderTableDao = orderTableDao;
        this.tableGroupRepository = tableGroupRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public TableGroupResponse create(final OrderTableRequest request) {
        validateRequestOrderTablesSize(request);

        final List<OrderTable> orderTables = findOrderTables(request);
        validateOrderTablesSize(request, orderTables);
        validateHaveNotEmptyOrderTable(orderTables);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroup.addOrderTables(orderTables);

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(savedTableGroup);
    }

    private void validateRequestOrderTablesSize(final OrderTableRequest request) {
        List<OrderTableRequestDto> orderTables = request.getOrderTables();
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> findOrderTables(final OrderTableRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables()
                .stream()
                .map(OrderTableRequestDto::getId)
                .collect(Collectors.toList());

        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    private void validateOrderTablesSize(final OrderTableRequest request, final List<OrderTable> savedOrderTables) {
        if (request.getOrderTables().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateHaveNotEmptyOrderTable(final List<OrderTable> orderTables) {
        boolean isNotEmptyOrderTable = orderTables.stream()
                .anyMatch(OrderTable::isNotEmpty);

        if (isNotEmptyOrderTable) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateCannotUngroupCondition(orderTables);

        for (final OrderTable orderTable : orderTables) {
            OrderTable updatedOrderTable = new OrderTable(orderTable.getId(), orderTable.getNumberOfGuests(), false);
            orderTableDao.save(updatedOrderTable);
        }
    }

    private void validateCannotUngroupCondition(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = extractOrderTableIds(orderTables);
        orderValidator.validateNotCookingAndMeal(orderTableIds);
    }

    private List<Long> extractOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
