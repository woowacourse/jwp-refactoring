package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateTableGroupCommand;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupDao tableGroupDao, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupDao = tableGroupDao;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupCommand command) {
        final List<Long> orderTableIds = command.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        // todo: tablegroup repository 안으로 보내기
        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(null, LocalDateTime.now(), savedOrderTables));
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(savedTableGroup.getId());
            savedOrderTable.changeEmpty(false);
            orderTableRepository.save(savedOrderTable);
        }
        return savedTableGroup;
        // repository 안으로 보내기
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.changeEmpty(false);
            orderTableRepository.save(orderTable);
        }
    }

}
