package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        List<OrderTable> savedOrderTables = getFilledOrderTables(tableGroupDto);
        TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));

        final Long tableGroupId = savedTableGroup.getId();
        savedOrderTables = savedOrderTables.stream()
                .map(orderTable -> new OrderTable(orderTable.getId(), tableGroupId,
                        orderTable.getNumberOfGuests(), false))
                .map(orderTableDao::save)
                .collect(Collectors.toList());

        savedTableGroup = new TableGroup(savedTableGroup.getId(),
                savedTableGroup.getCreatedDate(), savedOrderTables);

        return TableGroupDto.from(savedTableGroup);
    }

    private List<OrderTable> getFilledOrderTables(TableGroupDto tableGroupDto) {
        final List<OrderTableDto> orderTableDtos = tableGroupDto.getOrderTables();

        if (CollectionUtils.isEmpty(orderTableDtos) || orderTableDtos.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTableDtos.stream()
                .map(OrderTableDto::getId)
                .collect(Collectors.toList());

        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        if (orderTableDtos.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
        return savedOrderTables;
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
