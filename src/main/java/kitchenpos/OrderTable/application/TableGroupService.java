package kitchenpos.OrderTable.application;

import kitchenpos.Order.domain.OrderStatus;
import kitchenpos.Order.domain.repository.OrderRepository;
import kitchenpos.OrderTable.domain.OrderTable;
import kitchenpos.OrderTable.domain.TableGroup;
import kitchenpos.OrderTable.domain.dto.request.TableGroupCreateRequest;
import kitchenpos.OrderTable.domain.repository.OrderTableRepository;
import kitchenpos.OrderTable.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
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

    public TableGroup create(final TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.orderTableIds();

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || savedOrderTable.isBelongToTableGroup()) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.enrollTableGroup(savedTableGroup.getId());
            savedOrderTable.changeEmptyStatus(false);
        }

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                orderTables, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.releaseTableGroup();
            orderTable.changeEmptyStatus(false);
        }
    }
}
