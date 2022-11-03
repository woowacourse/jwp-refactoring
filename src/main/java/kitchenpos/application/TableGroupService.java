package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableDao orderTableDao,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final List<Long> orderTableIds = tableGroupCreateRequest.mapToOrderTableIds();

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        tableGroup.validateOrderTableSize(orderTableIds);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.validateToCreate();
        }

        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        validateOrderStatus(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }

    private void validateOrderStatus(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("완료되지 않은 주문이 존재합니다.");
        }
    }
}
