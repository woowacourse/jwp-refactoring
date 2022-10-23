package kitchenpos.application;

import kitchenpos.application.dto.CreateTableGroupDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    @Transactional
    public TableGroupDto create(CreateTableGroupDto createTableGroupDto) {
        List<Long> tableIds = createTableGroupDto.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(tableIds);
        if (tableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        OrderTables orderTables = OrderTables.ofNotGroupedOrderTables(savedOrderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup());
        orderTables.group(savedTableGroup.getId());
        for (final OrderTable savedOrderTable : orderTables.getValue()) {
            orderTableDao.save(savedOrderTable);
        }
        return TableGroupDto.of(savedTableGroup, orderTables.getValue());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}
