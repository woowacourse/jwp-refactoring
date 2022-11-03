package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderTableIdDto;
import kitchenpos.order.application.dto.TableGroupCreationDto;
import kitchenpos.order.application.dto.TableGroupDto;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao,
                             final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupDto create(final TableGroupCreationDto tableGroupCreationDto) {
        List<Long> orderTableIds = getOrderTableIds(tableGroupCreationDto);
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateTheNumberOfOrderTableIds(orderTableIds, savedOrderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(savedOrderTables, LocalDateTime.now()));
        final Long tableGroupId = savedTableGroup.getId();

        final List<OrderTable> orderTables = savedOrderTables.stream()
                .map(orderTable -> orderTableDao.save(
                        new OrderTable(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(), false)))
                .collect(Collectors.toList());

        return TableGroupDto.from(savedTableGroup.addTableGroups(orderTables));
    }

    private List<Long> getOrderTableIds(final TableGroupCreationDto tableGroupCreationDto) {
        return tableGroupCreationDto.getOrderTableIds()
                .stream()
                .map(OrderTableIdDto::getId)
                .collect(Collectors.toList());
    }

    private void validateTheNumberOfOrderTableIds(final List<Long> orderTableIds,
                                                  final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }


    @Transactional
    public void ungroupTable(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateOrdersStatus(orderDao.findAllByOrderTableId(orderTableIds));

        for (final OrderTable orderTable : orderTables) {
            orderTableDao.save(new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false));
        }
    }

    private void validateOrdersStatus(final List<Order> orders) {
        if (isUnCompletionStatus(orders)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isUnCompletionStatus(final List<Order> orders) {
        return orders.stream()
                .anyMatch(order -> !order.isInCompletionStatus());
    }
}
