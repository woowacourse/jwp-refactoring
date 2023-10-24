package kitchenpos.application;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupDao tableGroupDao) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정할 수 있는 테이블은 2개이상부터 입니다.");
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findByIds(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("그룹화 하려는 테이블이 중복되거나 존재하지 않습니다");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("비어있지 않은 테이블이나, 이미 그룹화 되어있는 테이블은 그룹화 할 수 없습니다");
            }
        }

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroupId(tableGroupId);
            savedOrderTable.updateOrderStatus(false);
            orderTableRepository.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("테이블 중 주문이 완료되지 않은 테이블이 있다면 그룹화를 해제할 수 없다");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.deleteTableGroupId();
            orderTable.updateOrderStatus(false);
            orderTableRepository.save(orderTable);
        }
    }
}
