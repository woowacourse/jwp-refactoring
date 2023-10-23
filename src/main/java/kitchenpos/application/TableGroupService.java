package kitchenpos.application;

import java.util.List;
import java.util.Set;
import kitchenpos.application.dto.TableGroupCreateDto;
import kitchenpos.application.exception.TableGroupAppException;
import kitchenpos.application.exception.TableGroupAppException.UngroupingNotPossibleException;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
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
        final Set<Order> findOrders = orderRepository.findAllByOrderTableIn(orderTables);

        return findOrders.stream()
            .anyMatch(orderTable -> orderTable.isSameStatus(OrderStatus.COOKING)
                || orderTable.isSameStatus(OrderStatus.MEAL));
    }
}
