package kitchenpos.application;

import kitchenpos.application.dto.request.CreateTableGroupDto;
import kitchenpos.application.dto.response.TableGroupDto;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(OrderRepository orderRepository,
                             OrderTableDao orderTableDao,
                             TableGroupDao tableGroupDao) {
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupDto create(CreateTableGroupDto createTableGroupDto) {
        final List<Long> tableIds = createTableGroupDto.getOrderTableIds();
        final OrderTables orderTables = findValidOrderTables(tableIds);
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup());
        orderTables.group(tableGroup.getId());
        List<OrderTable> groupedTables = orderTables.getValue();
        for (final OrderTable savedOrderTable : groupedTables) {
            orderTableDao.save(savedOrderTable);
        }
        return TableGroupDto.of(tableGroup, groupedTables);
    }

    private OrderTables findValidOrderTables(List<Long> tableIds) {
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(tableIds);
        if (tableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
        return new OrderTables(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateNoOngoingOrder(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.removeTableGroupId();
            orderTableDao.save(orderTable);
        }
    }

    private void validateNoOngoingOrder(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.getOngoingStatuses())) {
            throw new IllegalArgumentException();
        }
    }
}
