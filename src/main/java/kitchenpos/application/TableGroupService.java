package kitchenpos.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.application.dto.TableGroupCreateDto;
import kitchenpos.application.exception.TableGroupAppException;
import kitchenpos.application.exception.TableGroupAppException.UngroupingNotPossibleException;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateDto tableGroupCreateDto) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(
            tableGroupCreateDto.getOrderTableIds());

        if (tableGroupCreateDto.getOrderTableIds().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        final TableGroup tableGroup = TableGroup.of(savedOrderTables);

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup findTableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(
                () -> new TableGroupAppException.NotFoundTableGroupException(tableGroupId));

        final boolean existCookingOrMeal = isExistCookingOrMeal(findTableGroup);

        if (existCookingOrMeal) {
            throw new UngroupingNotPossibleException();
        }

        findTableGroup.ungroup();

        tableGroupRepository.save(findTableGroup);
    }

    private boolean isExistCookingOrMeal(final TableGroup findTableGroup) {
        final List<OrderTable> orderTables = findTableGroup.getOrderTables();
        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        final Set<Order> findOrders = orderRepository.findAllByOrderTableIdIn(orderTableIds);

        return findOrders.stream()
            .anyMatch(orderTable -> orderTable.isSameStatus(OrderStatus.COOKING)
                || orderTable.isSameStatus(OrderStatus.MEAL));
    }
}
