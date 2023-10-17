package kitchenpos.application;

import static kitchenpos.exception.TableGroupExceptionType.CAN_NOT_UNGROUP_COOKING_OR_MEAL;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.tablegroup.CreateTableGroupCommand;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.exception.TableGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(CreateTableGroupCommand command) {
        List<Long> orderTableIds = command.orderTableIds();

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.empty() || Objects.nonNull(savedOrderTable.tableGroup())) {
                throw new IllegalArgumentException();
            }
        }

        TableGroup tableGroup = new TableGroup();
        savedOrderTables.forEach(it -> it.setEmpty(false));
        savedOrderTables.forEach(tableGroup::addOrderTable);
        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::id)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new TableGroupException(CAN_NOT_UNGROUP_COOKING_OR_MEAL);
        }
        // [TODO] orderTable tableGroupId, setEmpty 설정
    }
}
