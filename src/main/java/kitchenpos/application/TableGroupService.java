package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional
@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroup create(final TableGroup tableGroup) {
        TableGroup group = TableGroup.of(LocalDateTime.now(), tableGroup.getOrderTables());
        final List<OrderTable> orderTables = group.getOrderTables();

        validateNumberOfOrderTable(orderTables);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        validateOrderTableSize(orderTables, savedOrderTables);
        validateOrderTableStatus(savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(group);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroup(savedTableGroup);
        }
        savedTableGroup.updateOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    private void validateOrderTableStatus(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException("orderTable 은 비어있어야 하고, 소속된 table group이 없어야 합니다.");
            }
        }
    }

    private void validateOrderTableSize(List<OrderTable> orderTables, List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 order table 입니다.");
        }
    }

    private void validateNumberOfOrderTable(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("order table 수는 2 이상이어야 합니다.");
        }
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 table group 입니다."));
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(null);
        }
    }
}
