package kitchenpos.table.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.GroupedTableCreateRequest;
import kitchenpos.table.dto.request.TableGroupCreateRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
        OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        List<GroupedTableCreateRequest> orderTableRequests = request.getOrderTables();

        if (CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2) {
            throw new IllegalArgumentException();
        }

        List<OrderTable> orderTables = extractOrderTables(orderTableRequests);
        TableGroup tableGroup = new TableGroup(orderTables);

        return tableGroupRepository.save(tableGroup);
    }

    private List<OrderTable> extractOrderTables(List<GroupedTableCreateRequest> requests){
        List<OrderTable> orderTables = new ArrayList<>();

        for (GroupedTableCreateRequest request : requests) {
            OrderTable orderTable = orderTableRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);
            orderTables.add(orderTable);
        }

        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);

        List<OrderTable> orderTables = tableGroup.getOrderTables();

        if(orderRepository.existsByOrderTableInAndOrderStatusIn(
            orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))){
            throw new IllegalArgumentException();
        }

        tableGroup.ungroup();
    }
}
