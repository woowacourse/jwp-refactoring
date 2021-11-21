package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = tableGroupRequest.toTableGroup();

        connectOrderTables(tableGroup);
        tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(tableGroup);
    }

    private void connectOrderTables(TableGroup tableGroup) {
        final OrderTables orderTables =
            new OrderTables(orderTableRepository.findAllByIdIn(tableGroup.getOrderTableIds()));

        orderTables.connect(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables =
            new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        validateCompletion(orderTables);
        orderTables.unGroupAll();
        tableGroupRepository.deleteById(tableGroupId);
    }

    private void validateCompletion(OrderTables orderTables) {
        final List<Long> tableIds = orderTables.getIds();
        final List<OrderStatus> notCompleted = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(tableIds, notCompleted)) {
            throw new IllegalArgumentException();
        }
    }
}
