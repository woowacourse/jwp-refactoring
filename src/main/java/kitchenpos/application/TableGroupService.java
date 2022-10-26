package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("등록되는 테이블 수가 2 이상이어야 한다.");
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("등록되는 모든 테이블들은 존재해야 한다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty()) {
                throw new IllegalArgumentException("등록되는 모든 테이블들은 비어있어야 한다.");
            }
            if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.");
            }
        }

        TableGroup timeSetTable = new TableGroup(null, LocalDateTime.now(), tableGroup.getOrderTables());

        final TableGroup savedTableGroup = tableGroupDao.save(timeSetTable);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroupId(tableGroupId);
            savedOrderTable.updateEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.updateOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupId(null);
            orderTable.updateEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}
