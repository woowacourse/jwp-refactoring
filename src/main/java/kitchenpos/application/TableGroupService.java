package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
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
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());

        List<OrderTable> orderTables = orderTableRepository.findAllById(request.getOrderTableIds());
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (OrderTable savedOrderTable : orderTables) {
            savedOrderTable.setTableGroup(tableGroup);
        }
        return TableGroupResponse.of(tableGroup, orderTables);
    }


    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        System.out.println(orderTableIds);

        final List<Order> all = orderRepository.findAll();
        all.forEach(it-> System.out.println(">>>"+ it.getId() +" "+ it.getOrderTable().getId()+ " "+it.getOrderStatus().name()));
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }
}
