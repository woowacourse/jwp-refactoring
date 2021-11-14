package kitchenpos.order.application;

import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(request.getOrderTableIds());
        if (orderTables.size() < 2) throw new IllegalArgumentException();

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.forEach(it -> it.group(tableGroup));
        return TableGroupResponse.of(tableGroup, orderTables);
    }


    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrder(orderTables);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateOrder(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
