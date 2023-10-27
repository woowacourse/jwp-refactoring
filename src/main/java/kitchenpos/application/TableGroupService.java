package kitchenpos.application;

import kitchenpos.domain.dto.TableGroupRequest;
import kitchenpos.domain.dto.TableGroupResponse;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> orderTableEntities = orderTableRepository.findAllByIdIn(request.getOrderTableIds());

        validateSavedOrderTable(request, orderTableEntities);

        final OrderTables orderTables = new OrderTables(orderTableEntities);
        final TableGroup tableGroup = new TableGroup(orderTables);

        tableGroup.group();

        tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(tableGroup);
    }

    private void validateSavedOrderTable(final TableGroupRequest request, final List<OrderTable> orderTableEntities) {
        if (request.getOrderTableIds().size() != orderTableEntities.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderTables orderTables = tableGroup.getOrderTables();

        final List<Long> orderTableIds = orderTables.getOrderTableIds();
        validateUngroupableStatus(orderTableIds);

        orderTables.ungroup();

        orderTableRepository.saveAll(orderTables.getValues());
    }

    private void validateUngroupableStatus(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
