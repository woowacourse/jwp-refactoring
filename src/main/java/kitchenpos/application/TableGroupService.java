package kitchenpos.application;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.response.CreateTableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public CreateTableGroupResponse create(final CreateTableGroupRequest request) {
        final List<OrderTable> orderTableEntities = getOrderTables(request.getOrderTableIds());
        TableGroup tableGroup = TableGroup.builder()
                .createdDate(LocalDateTime.now())
                .orderTables(orderTableEntities)
                .build();
        final TableGroup entity = tableGroupDao.save(tableGroup);
        final Long tableGroupId = entity.getId();

        List<OrderTable> orderTables = getFilledOrderTables(orderTableEntities, tableGroupId);
        TableGroup updated = entity.updateOrderTables(orderTables);
        return CreateTableGroupResponse.from(updated);
    }

    private List<OrderTable> getFilledOrderTables(List<OrderTable> orderTableEntities, Long tableGroupId) {
        return orderTableEntities.stream()
                .map(savedOrderTable -> savedOrderTable.fillTable(tableGroupId))
                .map(orderTableDao::save)
                .collect(Collectors.toList());
    }

    private List<OrderTable> getOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableIds.stream()
                .map(orderTableDao::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        validateOrderTables(orderTables);
        return orderTables;
    }

    private static void validateOrderTables(List<OrderTable> orderTables) {
        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
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
            OrderTable updated = orderTable.ungroup();
            orderTableDao.save(updated);
        }
    }
}
